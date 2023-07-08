<script lang="ts">
  import { onMount } from "svelte";

  let showButton = false;

  
  function checkScrollPosition() {
    showButton = window.pageYOffset > 200;
  }

  onMount(() => {
    window.addEventListener('scroll', checkScrollPosition);
  });


  async function scrollToTop() {
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

</script>

{#if showButton}
<div class="fixed py-2 bottom-0 right-4 text-white flex items-center">
  <button class="bg-ui px-3 py-3 ml-2 rounded-full hover:bg-hover animate-fade-up animate-duration-150" on:click={scrollToTop}>
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="w-6 h-6">
      <path d="M12 19V5M5 12l7-7 7 7" />
    </svg>
  </button>
</div>
{/if}