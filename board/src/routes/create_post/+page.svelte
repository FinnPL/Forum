<script lang="ts">
  import { Form, FormGroup, Input} from "sveltestrap";
  import "bootstrap/dist/css/bootstrap.min.css";
  import { Button } from "sveltestrap";
  import { token, cookie_name } from "../../lib/Login/login.js";
  import { getCookie } from "../../lib/functions";
  import { onMount } from "svelte";
  import { ip } from "../../lib/const.js"
  import  Error  from "../../lib/Error/error.svelte"
  let post_title: string;
  let post_body: string;
  let tokenValue: string;
  let cookie_name_value: string;
  let error = false;

  async function checkLoggedIn() {
    cookie_name_value = await getCookie("username");
    cookie_name.set(cookie_name_value);
    tokenValue = await getCookie("tokenValue");
    token.set(tokenValue);
  }

  onMount(async () => {
    await checkLoggedIn();
    await subStores();
  });

  async function subStores() {
    token.subscribe((value: string) => {
      tokenValue = value;
      console.log(tokenValue);
    });

    cookie_name.subscribe((value: string) => {
      cookie_name_value = value;
      console.log(cookie_name_value);
    });
  }

  async function post() {
    error = false;
    const res = await fetch(ip + "api/v1/post/add", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + tokenValue,
      },
      body: JSON.stringify({
        title: post_title,
        content: post_body,
      }),
    });
    if (!res.ok) {
      error = true;
    }
    return res.json();
  }
</script>

{#if error}
<Error></Error>

{/if}

<div class="container">
  <div class="alert alert-dark" role="alert">
    <Form>
      <FormGroup floating label="Titel">
        <Input placeholder="Titel" required bind:value={post_title} />
      </FormGroup>

      <FormGroup>
        <div class="form-floating">
          <textarea
            class="form-control"
            placeholder="Body"
            bind:value={post_body}
            id="floatingTextarea2"
            style="height: 100px"
          />
          <label for="floatingTextarea2">Text</label>
        </div>
      </FormGroup>

      <Button color="primary" on:click={post}>Post</Button>
    </Form>
  </div>
</div>

<style>
  .container {
    max-width: 700px;
    margin: 0 auto;
    padding: 2rem;
    text-align: center;
  }
  textarea {
    width: 100%;
    height: 300px;
  }

  .container {
    padding: 10px 16px;
    margin: 40px auto;
    max-width: 800px;
  }
</style>
