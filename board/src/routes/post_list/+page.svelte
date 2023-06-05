 <script lang="ts">
  import { onMount } from "svelte";
  import { token } from "../../lib/Login/login";
  import { default as defaultAvatar } from "../../lib/assets/defaultAvatar.png";

  let postList: any = [];
  let page: number = 0;
  let tokenValue: string;
  let ip: string;
  let canScroll = true;
  let profilePictureMap = new Map();

  async function get_server_ip() {
    ip = "http://" + location.hostname + ":8080/";
  }

  token.subscribe((value: string) => {
    tokenValue = value;
  });

  async function getFirstPostList() {
  const dataRes = await fetch(ip + "api/v1/post/page/" + page, {
    method: "GET",
    headers: { Authorization: "Bearer " + tokenValue },
  });
  const data = await dataRes.json();

  for (const post of data) {
    if(profilePictureMap.has(post.user_id)) {
      post.avatarSrc = profilePictureMap.get(post.user_id);
    } else {
      await fetchProfilePicture(post);
      profilePictureMap.set(post.user_id, post.avatarSrc);
    }
  }

  postList = data;
}

async function getPostList() {
  const dataRes = await fetch(ip + "api/v1/post/page/" + page, {
    method: "GET",
    headers: { Authorization: "Bearer " + tokenValue },
  });
  const data = await dataRes.json();

  for (const post of data) {
    if(profilePictureMap.has(post.user_id)) {
      post.avatarSrc = profilePictureMap.get(post.user_id);
    } else {
      await fetchProfilePicture(post);
      profilePictureMap.set(post.user_id, post.avatarSrc);
    }
  }


  postList = postList.concat(data);
}

async function fetchProfilePicture(post: { user_id: string; avatarSrc: string | null; }) {
  const profilePictureRes = await fetch(
    ip + "api/v1/file/profile/" + post.user_id,
    {
      method: "GET",
      headers: { Authorization: "Bearer " + tokenValue },
    }
  );

  if (profilePictureRes.ok) {
    const blob = await profilePictureRes.blob();
    const url = URL.createObjectURL(blob);
    post.avatarSrc = url;
  } else {
    post.avatarSrc = null;
  }
}

  async function scrollTimeout() {
    canScroll = !canScroll;

    if (!canScroll) setTimeout(scrollTimeout, 1000);
  }

  onMount(async () => {
    await get_server_ip();
    getFirstPostList();

    window.onscroll = function () {
      if (canScroll) {
        if (
          window.innerHeight + window.pageYOffset >=
          document.body.offsetHeight
        ) {
          page += 1;
          scrollTimeout();
          getPostList();
        }
      }
    };
  });

  function scrollToTop() {
    window.scrollTo({ top: 0, behavior: "smooth" });
  }
  
</script>

{#if postList != undefined}
  <div class="fixed py-2 bottom-0 right-4 text-white">
    <a href="/create_post" class="button bg-ui px-5 py-3 rounded-full hover:bg-hover">Post erstellen</a>

    <button class="bg-ui px-3 py-3 rounded-full hover:bg-hover" on:click={scrollToTop}>
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-6 h-6">
        <path d="M12 19V5M5 12l7-7 7 7" />
      </svg>
    </button>
  </div>


  {#each postList as post (post.id)}
    <div class="container mx-auto py-5 max-w-5xl">
      <a class="bg-postBG flex rounded-md px-5 py-5 border-2 border-border hover:border-hover" href={"/post/" + post.id}>
        <div>
          <div class="font-semibold text-xl flex">
            <a href={"/profile/" + post.user_id}>
              {#if post.avatarSrc}
                <img class="rounded-full" src={post.avatarSrc} alt="Avatar" width="50" height="50" />
              {:else}
                <img class="rounded-full" src={defaultAvatar} alt="Avatar" width="50" height="50" />
              {/if} 
            </a>
            <span class="pl-3 pt-2"> {post.title}</span>
            <a class="pl-5 pt-3.5 text-text text-sm" href={"/profile/" + post.user_id}>{post.user_name}</a>
            <span class="pl-1 pt-3.5 text-text text-sm">• {post.date}</span>
          </div>
          <div class="py-3">{post.content}</div><br />
        </div>
      </a>
    </div>
  {/each}
{/if}