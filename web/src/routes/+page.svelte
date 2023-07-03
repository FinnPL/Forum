<script lang="ts">
  import Login from "../lib/Login/login.svelte";
  import Post_List from "./post_list/+page.svelte";
  import { store_token } from "$lib/stores";
</script>

<Login show_sign_up="false" />

{#if $store_token != undefined}
  <Post_List />
  {:else}
  <Login show_sign_up="false" />
{/if}
