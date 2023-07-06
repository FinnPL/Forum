<script lang="ts">
  import { onMount } from "svelte";
  import PostItem from '../../lib/PostItem.svelte';
  import { fetchPage, fetchProfilePicture, formatDate } from "../../lib/functions";
  import { store_token } from "$lib/stores";  

  let postList: any = [];
  let page: number = 0;
  let ip: string;
  let canScroll = true;
  let showScrollButton = false;

  async function get_server_ip() {
    ip = "http://" + location.hostname + ":8080/";
  }

  async function getPostList() {
    const dataRes = await fetchPage("api/v1/post/page/","GET", page);

    for (const post of dataRes) {
      post.avatarSrc = await fetchProfilePicture(ip, $store_token, post);
      post.date = await formatDate(post.date);
    }

    postList = postList.concat(dataRes);
  }

  async function scrollTimeout() {
    canScroll = !canScroll;

    if (!canScroll) setTimeout(scrollTimeout, 500);
  }

  onMount(async () => {
    await get_server_ip();
    getPostList();

    window.onscroll = function () {
      const scrollPosition = window.pageYOffset || document.documentElement.scrollTop;
      showScrollButton = scrollPosition > 100;
      if (canScroll) {
        if (
          window.innerHeight + window.pageYOffset >=
          document.body.offsetHeight - 500
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

{#if postList !== undefined}
  <div class="fixed py-2 bottom-0 right-4 text-white flex items-center">
    <a href="/create_post" class="button bg-ui px-5 py-3 rounded-full hover:bg-hover">Post erstellen</a>

    {#if showScrollButton}
        <button class="bg-ui px-3 py-3 ml-2 rounded-full hover:bg-hover animate-fade-up animate-duration-150" on:click={scrollToTop}>
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-6 h-6">
            <path d="M12 19V5M5 12l7-7 7 7" />
          </svg>
        </button>
    {/if}
  </div>


  {#each postList as post (post.id)}
    <PostItem post={post}/>
  {/each}
{/if}