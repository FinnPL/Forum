<script lang="ts">
  import { token, cookie_name, own_user_id } from "./login";
  import { Button, Input } from "sveltestrap";
  import "bootstrap/dist/css/bootstrap.min.css";
  import { onMount } from "svelte";
  import { getCookie } from "../functions";
  import {ip} from "../const.js"
  let user_name: string;
  let password: string;
  let tokenValue: string;
  let cookie_name_value: string;
  let own_user_id_value: string;

  async function checkLoggedIn() {
    cookie_name_value = await getCookie("username");
    cookie_name.set(cookie_name_value);
    tokenValue = await getCookie("tokenValue");
    token.set(tokenValue);
    own_user_id_value = await getCookie("userid");
    own_user_id.set(own_user_id_value);
  }

  async function signUp() {
    const res = await fetch(ip + "api/v1/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ user_name, password }),
    });
    const data = await res.json();
    token.set(data.token);
    token.subscribe((token: any) => {
      tokenValue = token;
      console.log(tokenValue + "TTTTTTTTT");
    });
    own_user_id.subscribe((temp: any) => {
      own_user_id_value = temp;
      console.log(tokenValue + "TTTTTTTTT");
    });
    own_user_id.set(data.user_id);

    document.cookie = "tokenValue=" + tokenValue;
    document.cookie = "username=" + user_name;
    document.cookie = "userid=" + own_user_id_value;

    console.log("Der Cookie ist:" + document.cookie);
    token.set(tokenValue);
    let name = await getCookie("username");
    console.log(name);
  }

  async function login() {
    const res = await fetch(ip + "api/v1/auth/authenticate", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ user_name, password }),
    });
    const data = await res.json();
    token.set(data.token);
    token.subscribe((token: any) => {
      tokenValue = token;
    });
    own_user_id.subscribe((temp: any) => {
      own_user_id_value = temp;
      console.log(tokenValue + "TTTTTTTTT");
    });
    own_user_id.set(data.user_id);

    document.cookie = "tokenValue=" + tokenValue;
    document.cookie = "username=" + user_name;
    document.cookie = "userid=" + own_user_id_value;

    console.log("Der Cookie ist:" + document.cookie);
    token.set(tokenValue);
    let name = await getCookie("username");
    console.log(name);
  }

  onMount(async () => {
    checkLoggedIn();
    if (document.cookie != undefined) {
      let tokenValue = await getCookie("tokenValue");
      token.set(tokenValue);
      let nameValue = await getCookie("username");
      cookie_name.set(nameValue);
      await subStores();
    }
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

</script>

{#if cookie_name_value != "undefined" || cookie_name_value != undefined}
  <div class="container">
    <h1>Eingeloggt als {cookie_name_value}</h1>
  </div>
{/if}

{#if cookie_name_value == "undefined" || cookie_name_value == undefined}
  <div class="container">
    <form on:submit|preventDefault>
      <Input placeholder="Username" type="text" bind:value={user_name} />
      <Input placeholder="Password" type="password" bind:value={password} />
      <Button color="primary" on:click={signUp} on:click={() => location.reload()} >Sign Up</Button>
      <Button color="primary" on:click={login} on:click={() => location.reload()} >Login</Button>
    </form>
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
