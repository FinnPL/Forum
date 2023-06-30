<script lang="ts">
  import { token, cookie_name, own_user_id } from "./login";
  import { onMount } from "svelte";
  import { getCookie } from "../functions";
  import { goto } from "$app/navigation";

  let password: string;
  let tokenValue: string;
  let cookie_name_value: string;
  let own_user_id_value: string;
  let ip: string;


  //Auth sachen:
  export let givenname:string;
  export let surname:string;
  export let classname:string;
  export let signature:string;
  export let login_name:string;
  let user_name = login_name;

  $: console.log(signature)

  export let show_sign_up = "true";



  async function get_server_ip() {
    ip = "http://" + location.hostname + ":8080/";
  }

  async function checkLoggedIn() {
    // Check if you already logged in
    cookie_name_value = await getCookie("username");
    cookie_name.set(cookie_name_value);
    tokenValue = await getCookie("tokenValue");
    token.set(tokenValue);
    own_user_id_value = await getCookie("userid");
    own_user_id.set(own_user_id_value);
  }

  async function signUp() {
    // Sign up & store the values in cookies

    console.log(JSON.stringify({ givenname,surname, classname, user_name, signature, password}),)

    const res = await fetch(ip + "api/v1/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ givenname,surname, classname, user_name, signature, password}),
    });
    
    const data = await res.json();
    token.set(data.token);
    token.subscribe((token: any) => {
      tokenValue = token;
    });
    own_user_id.subscribe((temp: any) => {
      own_user_id_value = temp;
    });
    own_user_id.set(data.user_id);

     document.cookie = "tokenValue=" + tokenValue+";path=/";
     document.cookie = "username=" + user_name+";path=/";
     document.cookie = "userid=" + own_user_id_value+";path=/";

    console.log("Der Cookie ist:" + document.cookie);
    token.set(tokenValue);
    let name = await getCookie("username");
    console.log(name);
    await goto("/");
    location.reload();
  }

  async function login() {
    //Login & store the values in cookies
    const res = await fetch(ip + "api/v1/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ user_name, password}),
    });
    const data = await res.json();
    token.set(data.token);
    token.subscribe((token: any) => {
      tokenValue = token;
    });
    own_user_id.subscribe((temp: any) => {
      own_user_id_value = temp;
    });
    own_user_id.set(data.user_id);

    document.cookie = "tokenValue=" + tokenValue;
    document.cookie = "username=" + user_name;
    document.cookie = "userid=" + own_user_id_value;

    console.log("Der Cookie ist:" + document.cookie);
    token.set(tokenValue);
    let name = await getCookie("username");
    console.log(name);
    await goto("/");
    location.reload();
  }

  onMount(async () => {
    // Write in Cookie values in writable stores
    await get_server_ip();
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
    // Subscribe to writable stores
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

{#if cookie_name_value == "undefined" || cookie_name_value == undefined}
  <div class="container">
    <form on:submit|preventDefault>

      <input class="text-black" placeholder="Username" type="text" bind:value={user_name} />
      <input class="text-black" placeholder="Password" type="password" bind:value={password} />

      {#if show_sign_up == "true"}
      <button on:click={signUp}>Sign Up </button>
      {/if}
      <button on:click={login}>Login </button>
    </form>
  </div>
{/if}

