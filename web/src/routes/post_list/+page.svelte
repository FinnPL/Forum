<script lang="ts">
  import { onMount } from "svelte";
  import { token } from "../../lib/Login/login";
  import PostItem from '../../lib/PostItem.svelte';
  import { fetchProfilePicture, formatDate } from "../../lib/functions";
  

  let postList: any = [];
  let page: number = 0;
  let tokenValue: string;
  let ip: string;
  let canScroll = true;

  async function get_server_ip() {
    ip = "http://" + location.hostname + ":8080/";
  }

  token.subscribe((value: string) => {
    tokenValue = value;
  });



  async function getPostList() {
    console.log(tokenValue+" SUS")
    const dataRes = await fetch(ip + "api/v1/post/page/" + page, {
      method: "GET",
      headers: { Authorization: "Bearer " + tokenValue },
    });
    const data = await dataRes.json();

    for (const post of data) {
      post.avatarSrc = await fetchProfilePicture(ip, tokenValue, post);
      post.date = await formatDate(post.date);
    }

    postList = postList.concat(data);
  }

  async function scrollTimeout() {
    canScroll = !canScroll;

    if (!canScroll) setTimeout(scrollTimeout, 1000);
  }

  onMount(async () => {
    await get_server_ip();
    getPostList();

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
    <PostItem post={post}/>
  {/each}
{/if}