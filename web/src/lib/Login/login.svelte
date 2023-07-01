<script lang="ts">
  import { token, cookie_name, own_user_id } from "./login";
  import { onMount } from "svelte";
  import { getCookie } from "../functions";
  import { goto } from "$app/navigation";
  import {signOut} from "../functions"
  import logoFull from "../assets/logoFull.png";

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

  async function submitForm() {
    await signOut()
    const form = document.getElementById('oauthTriggerForm') as HTMLFormElement;
    form.submit();
    
  }
</script>

{#if cookie_name_value == "undefined" || cookie_name_value == undefined}
  <div class="flex flex-col items-center justify-center mx-auto px-60 md:h-screen">
    <span class="flex items-center justify-center mb-6">
      <img src={logoFull} class="w-1/2" alt="Forum"/>
    </span>

    <div class="w-full max-w-lg bg-postBG rounded-lg border border-border">
      <div class="p-6 space-y-4">
        {#if show_sign_up == "true"}
          <h1 class="font-bold text-xl leading-tight tracking-tight">Passwort erstellen</h1>
        {:else}
          <h1 class="font-bold text-xl leading-tight tracking-tight">Melde dich mit deinem Account an</h1>
        {/if}

        <form class="space-y-3" on:submit|preventDefault>
          <div class="pt-2">
            <label for="username" class="block mb-2 text-sm font-medium">Nutzername</label>
            {#if show_sign_up == "true"}
              <input class="text-white bg-ui border border-border rounded-lg block w-full p-2.5 pointer-events-none" type="text" bind:value={user_name} />
            {:else}
              <input class="text-white bg-ui border border-border rounded-lg block w-full p-2.5" type="text" bind:value={user_name} />
            {/if}
          </div>

          <div class="pt-2">
            <label for="password" class="block mb-2 text-sm font-medium">Passwort</label>
            <input class="text-white bg-ui border border-border rounded-lg block w-full p-2.5" type="password" bind:value={password} />
          </div>

          {#if show_sign_up == "true"}
            <div class="pt-2">
              <button class="bg-primary py-3 rounded-lg w-full hover:brightness-75" on:click={signUp}>Sign Up</button>
            </div>
          {:else}
            <div class="flex items-end justify-end">
              <button class="hover:underline text-primary" on:click={submitForm}>Passwort vergessen?</button>
            </div>
            <button class="bg-primary py-3 rounded-lg w-full hover:brightness-75" on:click={login}>Login</button>
          {/if}
        </form>

        {#if show_sign_up == "false"}
          <div class="pt-5">
            <hr class="h-0.5 border-t-0 bg-text" />
          </div>
          <h1 class="py-3 font-bold text-xl leading-tight tracking-tight">Erstelle einen neuen Account</h1>
          <button class="bg-primary py-3 rounded-lg w-full hover:brightness-75" on:click={submitForm}>Authentifizierung</button>  
          <form id="oauthTriggerForm" action="https://ghse.de/auth/auth.php" method="post">
            <input type="hidden" name="application" value="GHSE_TGI_Forum"/>
            <input type="hidden" name="clientID" value="GHSE_TGI_Forum"/>
            <input type="hidden" name="response_type" value="data"/>
            <input type="hidden" name="state" value="6484ab38ad017"/>
          </form>
        {/if}
      </div>
    </div>
  </div>
{/if}
