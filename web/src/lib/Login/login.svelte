<script lang="ts">
  import { onMount } from "svelte";
  import { fetcher, getCookie } from "../functions";
  import { goto } from "$app/navigation";
  import {signOut} from "../functions";
  import { passwordStrength } from 'check-password-strength'
  import logoFull from "../assets/logoFull.png";
  import {store_username,store_userid, store_token, store_user_role} from "../stores";

  
  let password: string;
  let confirmPassword: string;
  let tokenValue: string;
  let ip: string;
  let passwordStrengthValue: string;
  let login_error: boolean = false;

  $: passwordStrengthValue = passwordStrength(password, [
    {
      id: 0,
      value: "Zu Schwach",
      minDiversity: 0,
      minLength: 0
    },
    {
      id: 1,
      value: "Schwach",
      minDiversity: 2,
      minLength: 6
    },
    {
      id: 2,
      value: "Mittel",
      minDiversity: 4,
      minLength: 8
    },
    {
      id: 3,
      value: "Stark",
      minDiversity: 4,
      minLength: 10
    }
  ]).value;

  //Auth sachen:
  export let givenname:string;
  export let surname:string;
  export let classname:string;
  export let signature:string;
  export let login_name:string;
  export let timestamp:string;
  let user_name = login_name;

  export let show_sign_up = "true";
  

  async function get_server_ip() {
    ip = "http://" + location.hostname + ":8080/";
  }

  async function checkLoggedIn() {
    // Check if you already logged in
    $store_username = await getCookie("username");
    $store_token = await getCookie("tokenValue");
    $store_userid = await getCookie("userid");
  }

  async function signUp() {
    // Sign up & store the values in cookies

    const res = await fetch(ip + "api/v1/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ givenname,surname, classname, user_name, timestamp, signature, password}),
    });
    
    const data = await res.json();
    $store_token = data.token;
    $store_userid = data.user_id;
    $store_username = user_name;
    $store_user_role = data.role;

     document.cookie = "tokenValue=" + $store_token+";path=/";
     document.cookie = "username=" + $store_username+";path=/";
     document.cookie = "userid=" + $store_userid+";path=/";
     document.cookie = "role=" + $store_user_role+";path=/";

    
    await goto("/");
    location.reload();
  }

  async function login() {
    //Login & store the values in cookies
    const res = await fetcher("api/v1/auth/login","POST", {user_name, password});
    if(res.status == 403) {
      login_error = true;
      password = "";
      return;
    }


    $store_token = res.token;
    $store_userid = res.user_id;
    $store_username = user_name;
    $store_user_role = res.role;

    //saves the details in cookies
    document.cookie = "tokenValue=" + $store_token+";path=/";
    document.cookie = "username=" + $store_username+";path=/";
    document.cookie = "userid=" + $store_userid +";path=/";
    document.cookie = "role=" + $store_user_role+";path=/";

 
    await goto("/");
    location.reload();
  }

  onMount(async () => {
    await get_server_ip();
    checkLoggedIn();
    
  });

  
  function handleKeyDown(event: any) {
    if (event.key === 'Enter' && (tokenValue === undefined || tokenValue === "") && show_sign_up === "false" && user_name && password) {
      event.preventDefault();
      login();
    }
  }

  onMount(()=>{
    document.addEventListener('keydown', handleKeyDown);
  })

  async function submitForm() {
    await signOut()
    const form = document.getElementById('oauthTriggerForm') as HTMLFormElement;
    form.submit();
    
  }
</script>

{#if $store_username == "undefined" || $store_username == undefined}
  <div class="flex flex-col items-center justify-center mx-auto px-60 h-screen">

    <div class="w-screen max-w-lg ">
      <a href="/" class="flex items-center justify-center mb-6">
        <img src={logoFull} class="w-1/2" alt="Forum"/>
      </a>

      <div class="p-6 space-y-4 bg-postBG rounded-lg border border-border">

        <h1 class="font-bold text-xl leading-tight tracking-tight"> 
            {#if show_sign_up == "true"}
              Passwort erstellen
            {:else}
              Melde dich mit deinem Account an
            {/if}
          </h1>

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
        
            <div class="flex items-center">
              {#if passwordStrengthValue == "Stark" || passwordStrengthValue == "Mittel"}
                <svg class={`w-4 h-4 mr-1 mt-0.5 ${passwordStrengthValue == "Stark" ? "stroke-green-500" : "stroke-yellow-500"}`} fill="none" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18L18 6M12 18l-4 -6"/>
                </svg>
                <p class={`${passwordStrengthValue == "Stark" ? "text-green-500" : "text-yellow-500"}`}>{passwordStrengthValue}</p>
              {:else}
                <svg class={`w-4 h-4 mr-1 mt-0.5 ${passwordStrengthValue == "Schwach" ? "stroke-red-400" : "stroke-red-500"}`} fill="none" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                </svg>
                <p class={`${passwordStrengthValue == "Schwach" ? "text-red-400" : "text-red-500"}`}>{passwordStrengthValue}</p>
              {/if}
            </div>

            <div class="pt-2">
              <label for="confirmPassword" class="block mb-2 text-sm font-medium">Passwort bestätigen</label>
              <input class="text-white bg-ui border border-border rounded-lg block w-full p-2.5" type="password" id="cpw" bind:value={confirmPassword} />

              <div class="pt-2 flex items-center">
                {#if password != confirmPassword}
                  <svg class="w-4 h-4 mr-1 mt-0.5 stroke-red-500" fill="none" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                  </svg>
                  <p class="text-red-500">Passwörter stimmen nicht überein</p>
                {/if}
              </div>
            </div>

            <div class="pt-2">
              <button class="loginPageButton" on:click={signUp} disabled={passwordStrengthValue !== "Mittel" && passwordStrengthValue !== "Stark" || password != confirmPassword || password == undefined}>Sign Up</button>
            </div>
          {:else}
            <div class="flex items-end justify-end">
              <button class="hover:underline text-primary" on:click={submitForm}>Passwort vergessen?</button>
            </div>
            <button class="loginPageButton" on:click={login} disabled={password == undefined || password == ""}>Login</button>
            
            {#if login_error}
              <div class="pt-5 flex items-center">
                <svg class="w-4 h-4 mr-1 mt-0.5 stroke-red-500" fill="none" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/>
                </svg>
                <p class="text-red-500">Nutzername oder Passwort inkorrekt</p>
              </div>
            {/if}
          {/if}
        </form>

        {#if show_sign_up == "false"}
          <div class="pt-5">
            <hr class="h-0.5 border-t-0 bg-text" />
          </div>
          <h1 class="py-3 font-bold text-xl leading-tight tracking-tight">Erstelle einen neuen Account</h1>
          <button class="loginPageButton" on:click={submitForm} >Authentifizierung</button>  
          <form id="oauthTriggerForm" action="https://ghse.de/auth/auth.php" method="post" on:keydown={handleKeyDown} >
            <input type="hidden" name="application" value="GHSE_TGI_Forum"/>
            <input type="hidden" name="clientID" value="GHSE_TGI_Forum"/>
            <input type="hidden" name="response_type" value="data"/>
            <input type="hidden" name="state" value={new Date().getTime()}/>
          </form>
        {/if}
      </div>
    </div>
  </div>
{/if}
