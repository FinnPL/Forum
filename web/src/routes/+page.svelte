<script lang="ts">
  import Login from "../lib/Login/login.svelte";
  import Post_List from "./post_list/+page.svelte";
  import { store_token } from "$lib/stores";
  import { getCookie } from "$lib/functions";

  async function getPromise() {
    return await getCookie("tokenValue");
  }

  let promise = getPromise();
</script>

{#await promise then token}
    {#if token != undefined}
      <Post_List />
      {:else}
        <Login show_sign_up="false" />
    {/if}
{/await}