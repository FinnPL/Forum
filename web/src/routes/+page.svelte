<script lang="ts">
  import Login from "../lib/Login/login.svelte";
  import Post_List from "./post_list/+page.svelte";
  import { store_token } from "$lib/stores";

  let promise = (async () => {
    return $store_token;
  })

  
</script>

{#await promise then token}
    {#if token !== undefined}
      <Post_List />
      {:else}
        <Login show_sign_up="false" />
    {/if}
{/await}