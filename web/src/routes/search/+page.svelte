<script lang="ts">
  import { onMount } from "svelte";
  import { fetchPage, formatDate, getCookie } from "../../lib/functions";
  import { page } from "$app/stores";
  import { default as defaultAvatar } from "../../lib/assets/defaultAvatar.png";
  import PostItem from "$lib/PostItem.svelte";
  import { fetchProfilePicture } from "../../lib/functions";
  import { goto } from "$app/navigation";
  import { store_search_input } from "$lib/stores";
  import ScrollButton from "$lib/ScrollButton/+page.svelte";
  let ip: string;

 

  async function get_server_ip() {
    ip = "http://" + location.hostname + ":8080/";
  }

  let searchList: any = [];
  let pageN: number = 0;
  let searchType: string;
  let tokenValue: string;
  let canScroll = true;
  $: $store_search_input = $page.url.searchParams.get("q") || '';
  $: type = $page.url.searchParams.get("type") || '';



  $: console.log(pageN)

  async function search_post() {
    console.log("Der Input "+$store_search_input)
    searchType = "Post";
    const dataRes = await fetchPage(
     "api/v1/post/search/" + $store_search_input + "/",
      "GET",
      pageN
    );

    for (const post of dataRes) {
      post.avatarSrc = await fetchProfilePicture(ip, tokenValue, post);
      post.date = await formatDate(post.date);
    }

    
    searchList = searchList.concat(dataRes);

    if (searchList.length == 0) {
      searchList[0] = "keinErgebnis";
    } 
    console.log(searchList);
  }

  
  async function search_user() {
    searchType = "User";
    const dataRes = await fetchPage(
      "api/v1/user/search/" + $store_search_input + "/",
      "GET",
      pageN
    );

    for (const user of dataRes) {
      user.user_id = user.id;
      user.avatarSrc = await fetchProfilePicture(ip, tokenValue, user);
    }

    searchList = searchList.concat(dataRes);

    if (searchList.length == 0) {
      searchList[0] = "keinErgebnis";
    }
    console.log(searchList);
  }

  async function scrollTimeout() {
    canScroll = !canScroll;

    if (!canScroll) setTimeout(scrollTimeout, 500);
  }

  onMount(async () => {
    await get_server_ip();
    tokenValue = await getCookie("tokenValue");
    if (type == "post" || type == '') search_post();
    if (type == "user") search_user();
    window.onscroll = function (ev) {
      // Dymamic loading of searchlist items
      if (canScroll)
        if (
          window.innerHeight + window.pageYOffset >=
          document.body.offsetHeight
        ) {
          if (searchType == "Post") {
            pageN
         += 1;
            search_post();
            scrollTimeout();
          }
          if (searchType == "User") {
            pageN
         += 1;
            search_user();
            scrollTimeout();
          }
        }
    };
  });

  async function resetPage() {
    pageN = 0;
    searchList = [];
  }

  async function search(type: string) {
    const params = new URLSearchParams($page.url.searchParams);
    if (type === "post") {
      params.set("type", "post");
      resetPage();
      search_post();
    } else if (type === "user") {
      params.set("type", "user");
      resetPage();
      search_user();
    }
    goto("/search?" + params.toString());
  }

  function scrollToTop() {
    window.scrollTo({ top: 0, behavior: "smooth" });
  }
  
</script>

<div class="container pt-5 mx-auto w-11/12 sm:max-w-5xl sm:w-full space-x-2">
  <button class="bg-ui hover:bg-hover rounded-full px-5 py-2" on:click={() => search("post")}>Posts</button>
  <button class="bg-ui hover:bg-hover rounded-full px-5 py-2" on:click={() => search("user")}>Benutzer</button>
</div>

<ScrollButton></ScrollButton>

{#if searchType === "Post" && searchList[0] !== "keinErgebnis"}
  {#each searchList as post (post.id)}
    <PostItem post={post}/>
  {/each}
{/if}

{#if searchType == "User" && searchList[0] != "keinErgebnis"}
  {#each searchList as user (user.id)}
    <div class="container mx-auto pt-5 w-11/12 sm:max-w-5xl sm:w-full">
      <a class="bg-postBG flex flex-col rounded-md px-5 py-2 border-2 border-border hover:border-hover" href={"/profile/" + user.id}>
        <div class="font-semibold text-xl flex">
          {#if user.avatarSrc}
            <img class="rounded-full" src={user.avatarSrc} alt="Avatar" width="50" height="50" />
          {:else}
            <img class="rounded-full" src={defaultAvatar} alt="Avatar" width="50" height="50" />
          {/if} 
          <div class="pl-5 truncate">
            <p> {user.user_name} </p>
            <p class="text-text text-sm truncate">{user.user_name}</p>
          </div>
        </div>
      </a>
    </div>
  {/each}
{/if}