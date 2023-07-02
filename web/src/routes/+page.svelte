<script lang="ts">
  import Login from "../lib/Login/login.svelte";
  import { token } from "../lib/Login/login";
  import Post_List from "./post_list/+page.svelte";
  import { onMount } from "svelte";


  let tokenValue: string;


  token.subscribe((value: string) => {
    tokenValue = value;
  });  

</script>


<Login show_sign_up="false" />

{#if tokenValue != undefined}
  <Post_List />
  {:else}
  <Login show_sign_up="false" />
{/if}
