<script lang="ts">
    import { goto } from "$app/navigation";
    import { getCookie } from "$lib/functions";
    import { onMount } from "svelte";
    import { default as defaultAvatar } from "../lib/assets/defaultAvatar.png";
    import logoFull from "../lib/assets/logoFull.png";
    import {signOut} from "../lib/functions"
    import "../app.css";

    let own_user_id_value: string;
    let cookie_name_value: string;
    let tokenValue: string;
    let avatarSrc: string | null = defaultAvatar;
    let ip: string;
    let input: string;

    async function get_server_ip() {
        ip = "http://" + location.hostname + ":8080/";
    }

    async function initial_load() {
        tokenValue = await getCookie("tokenValue");
        own_user_id_value = await getCookie("userid");
        cookie_name_value = await getCookie("username");
    }


    

    async function gotoProfile() {
      const path = window.location.pathname.split("/");
      const userid = path[path.length - 1]; // Get userid from url path
      if (userid != tokenValue) {
        await goto("/profile/" + tokenValue);
        location.reload();
      }
    }

    async function gotoSearch() {
      if(input == "" || input == undefined) return false;
      window.location.href = `/search?q=${input}`;
    }

  onMount(async () => {
      await get_server_ip();
      await initial_load();
      const currentURL = window.location.href
      if(!currentURL.includes("/noeskauth/")) {
        if (tokenValue == undefined && location.pathname != "/") {
          await goto("/");
          location.reload();
        } else {
          const profilePictureRes = await fetch(
          ip + "api/v1/file/profile/" + own_user_id_value + "?" + new Date().getTime(),
            {
              method: "GET",
              headers: { Authorization: "Bearer " + tokenValue },
            }
          );

          if (profilePictureRes.ok) {
            const blob = await profilePictureRes.blob();
            const url = URL.createObjectURL(blob);
            avatarSrc = url;
          }
        }
      }
    });


</script>

{#if own_user_id_value != undefined && own_user_id_value != "undefined"}
  <div class="sticky top-0 bg-gradient-to-r from-primary to-secondary p-0.5 px-0 pt-0">
    <nav class="flex w-full items-center max-h-12 p-4 bg-postBG">
        <div class="flex-1 flex justify-start">
          <a href="/">
            <img src={logoFull} alt="Logo" class="h-8 w-auto">
          </a>
        </div>

        <form class="flex items-center max-w-5xl mx-auto bg-hover border border-border rounded-full">
          <input id="search" type="text" class="w-screen bg-border outline-none rounded-full" placeholder="Suchen..." bind:value={input}>
          <button id="searchButton" class="px-2 rounded-full text-white" on:click={gotoSearch}>
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ffffff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <circle cx="11" cy="11" r="8"></circle>
              <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
            </svg>
          </button>
        </form>

        <div class="flex-1 flex justify-end items-center group">
          <span class="mr-2">{cookie_name_value}</span>
          <img src={avatarSrc} alt={defaultAvatar} class="h-10 w-10 rounded-full">

          <div class="absolute hidden group-hover:block pt-28">
            <div class="bg-postBG">
              <ul>
                <li><a href={"/profile/" + own_user_id_value} on:click={gotoProfile}>Profil</a></li>
                <li class="pt-2"><a href="/" on:click={signOut}>Abmelden</a></li>
              <ul>
            </div>
          </div>
        </div>
    </nav>
  </div>
{/if}

<slot />
