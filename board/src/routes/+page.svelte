<script lang="ts">
  import Login from "../lib/Login/login.svelte";
  import { token } from "../lib/Login/login";
  import Post_List from "./post_list/+page.svelte";
  
  let tokenValue: string;

  token.subscribe((value: string) => {
    tokenValue = value;
  });
  


</script>

<Login />

{#if tokenValue != undefined }
  <Post_List />
{/if}
