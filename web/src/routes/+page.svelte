<script lang="ts">
  import Login from "../lib/Login/login.svelte";
  import Post_List from "./post_list/+page.svelte";
  import { store_token } from "$lib/stores";
  import { getCookie } from "$lib/functions";


  /* The code above does the following:
1. Uses the await block to wait for the promise to resolve.
2. It then checks if the token is undefined, meaning the cookie is not set.
3. If the token is undefined, it renders the Login component.
4. If the token is defined, it renders the Post_List component. */

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