<script lang="ts">
  import Login from "../lib/Login/login.svelte";
  import { token } from "../lib/Login/login";
  import Post_List from "./post_list/+page.svelte";
  import { onMount } from "svelte";
  import { Button } from "sveltestrap";

  let tokenValue: string;

  

  token.subscribe((value: string) => {
    tokenValue = value;
  });

  function submitForm() {
    const form = document.getElementById('oauthTriggerForm') as HTMLFormElement;
    form.submit();
  }
  
</script>

<Login show_sign_up="false" />

{#if tokenValue != undefined}
  <Post_List />

  {:else }
  <div class="container">

    <form id="oauthTriggerForm" action="https://ghse.de/auth/auth.php" method="post">
      <input type="hidden" name="application" value="GHSE_TGI_Forum"/>
      <input type="hidden" name="clientID" value="GHSE_TGI_Forum"/>
      <input type="hidden" name="response_type" value="data"/>
      <input type="hidden" name="state" value="6484ab38ad017"/>
  </form>

  
  <Button color="primary" on:click={submitForm}>Authentifizierung</Button>

  
</div>
  {/if}

  <style>
    .container {
      max-width: 400px;
      margin: 0 auto;
      padding: 2rem;
      text-align: center;
    }
  </style>