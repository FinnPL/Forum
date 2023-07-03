<script lang="ts">
    import { goto } from "$app/navigation";
    import { getCookie } from "$lib/functions";
    import { onMount } from "svelte";
    import { default as defaultAvatar } from "../lib/assets/defaultAvatar.png";
    import logoFull from "../lib/assets/logoFull.png";
    import {signOut} from "../lib/functions"
    import "../app.css";
    import { store_token, store_userid, store_username } from "$lib/stores";

    let avatarSrc: string | null = defaultAvatar;
    let ip: string;
    let input: string;

    async function get_server_ip() {
        ip = "http://" + location.hostname + ":8080/";
    }

    async function initial_load() {
        $store_token = await getCookie("tokenValue");
        $store_userid = await getCookie("userid");
        $store_username = await getCookie("username");
    }


    
    async function gotoProfile() {
        await goto("/profile/" + $store_userid);
        location.reload();
      
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
        if ($store_token == undefined && location.pathname != "/") {
          await goto("/");
          location.reload();
        } else {
          const profilePictureRes = await fetch(
          ip + "api/v1/file/profile/" + $store_userid + "?" + new Date().getTime(),
            {
              method: "GET",
              headers: { Authorization: "Bearer " + $store_token },
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

{#if $store_userid != undefined && $store_userid != "undefined"}
  <div class="sticky top-0 bg-gradient-to-r from-primary to-secondary p-0.5 px-0 pt-0">
    <nav class="flex w-full items-center max-h-12 p-4 bg-postBG">
      <div class="flex-1 flex justify-start">
        <a href="/">
          <img src={logoFull} alt="Logo" class="px-1 h-8 w-auto">
        </a>
      </div>

      <form class="flex items-center max-w-5xl mx-auto bg-hover border border-border rounded-full">
        <input id="search" type="text" class="w-screen bg-border outline-none rounded-full" placeholder="Suchen..." bind:value={input}>
        <button id="searchButton" class="pl-1 pr-2.5 rounded-full text-white" on:click={gotoSearch}>
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#ffffff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="8"></circle>
            <line x1="21" y1="21" x2="16.65" y2="16.65"></line>
          </svg>
        </button>
      </form>

      <div class="flex-1 flex justify-end">
        <div class="group">
          <div class="flex items-center">
            <span class="mr-2">{$store_username}</span>
            {#if avatarSrc}
              <img src={avatarSrc} alt="Avatar" class="h-10 w-10 rounded-full">
            {:else}
              <img src={defaultAvatar} alt="Avatar" class="h-10 w-10 rounded-full">
            {/if}
          </div>
          <div class="absolute hidden group-hover:block pt-2 right-3">
            <div class="bg-ui border border-border rounded-md">
              <ul>
                <a href={"/profile/" + $store_userid} on:click={gotoProfile}>
                  <li class="hover:bg-hover pr-4 flex items-center">
                    <svg fill="#ffffff" height="50px" width="50px" version="1.1" id="Capa_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="-482.9 -482.9 1448.70 1448.70" xml:space="preserve">
                      <g id="SVGRepo_bgCarrier" stroke-width="0"/>
                      <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"/>     
                      <g id="SVGRepo_iconCarrier"> <g> <g> <path d="M239.7,260.2c0.5,0,1,0,1.6,0c0.2,0,0.4,0,0.6,0c0.3,0,0.7,0,1,0c29.3-0.5,53-10.8,70.5-30.5 c38.5-43.4,32.1-117.8,31.4-124.9c-2.5-53.3-27.7-78.8-48.5-90.7C280.8,5.2,262.7,0.4,242.5,0h-0.7c-0.1,0-0.3,0-0.4,0h-0.6 c-11.1,0-32.9,1.8-53.8,13.7c-21,11.9-46.6,37.4-49.1,91.1c-0.7,7.1-7.1,81.5,31.4,124.9C186.7,249.4,210.4,259.7,239.7,260.2z M164.6,107.3c0-0.3,0.1-0.6,0.1-0.8c3.3-71.7,54.2-79.4,76-79.4h0.4c0.2,0,0.5,0,0.8,0c27,0.6,72.9,11.6,76,79.4 c0,0.3,0,0.6,0.1,0.8c0.1,0.7,7.1,68.7-24.7,104.5c-12.6,14.2-29.4,21.2-51.5,21.4c-0.2,0-0.3,0-0.5,0l0,0c-0.2,0-0.3,0-0.5,0 c-22-0.2-38.9-7.2-51.4-21.4C157.7,176.2,164.5,107.9,164.6,107.3z"/> <path d="M446.8,383.6c0-0.1,0-0.2,0-0.3c0-0.8-0.1-1.6-0.1-2.5c-0.6-19.8-1.9-66.1-45.3-80.9c-0.3-0.1-0.7-0.2-1-0.3 c-45.1-11.5-82.6-37.5-83-37.8c-6.1-4.3-14.5-2.8-18.8,3.3c-4.3,6.1-2.8,14.5,3.3,18.8c1.7,1.2,41.5,28.9,91.3,41.7 c23.3,8.3,25.9,33.2,26.6,56c0,0.9,0,1.7,0.1,2.5c0.1,9-0.5,22.9-2.1,30.9c-16.2,9.2-79.7,41-176.3,41 c-96.2,0-160.1-31.9-176.4-41.1c-1.6-8-2.3-21.9-2.1-30.9c0-0.8,0.1-1.6,0.1-2.5c0.7-22.8,3.3-47.7,26.6-56 c49.8-12.8,89.6-40.6,91.3-41.7c6.1-4.3,7.6-12.7,3.3-18.8c-4.3-6.1-12.7-7.6-18.8-3.3c-0.4,0.3-37.7,26.3-83,37.8 c-0.4,0.1-0.7,0.2-1,0.3c-43.4,14.9-44.7,61.2-45.3,80.9c0,0.9,0,1.7-0.1,2.5c0,0.1,0,0.2,0,0.3c-0.1,5.2-0.2,31.9,5.1,45.3 c1,2.6,2.8,4.8,5.2,6.3c3,2,74.9,47.8,195.2,47.8s192.2-45.9,195.2-47.8c2.3-1.5,4.2-3.7,5.2-6.3 C447,415.5,446.9,388.8,446.8,383.6z"/> </g> </g> </g> 
                    </svg>

                    <span class="pb-1" > Profil </span>
                  </li>
                </a>

                <a class=""href="/" on:click={signOut}>
                  <li class="hover:bg-hover pr-4 flex items-center">
                    <svg fill="#ffffff" height="50px" width="50px" version="1.1" id="Capa_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="-471.2 -471.2 1413.60 1413.60" xml:space="preserve">
                      <g id="SVGRepo_bgCarrier" stroke-width="0"/>
                      <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"/>
                      <g id="SVGRepo_iconCarrier"> <g> <g> <path d="M227.619,444.2h-122.9c-33.4,0-60.5-27.2-60.5-60.5V87.5c0-33.4,27.2-60.5,60.5-60.5h124.9c7.5,0,13.5-6,13.5-13.5 s-6-13.5-13.5-13.5h-124.9c-48.3,0-87.5,39.3-87.5,87.5v296.2c0,48.3,39.3,87.5,87.5,87.5h122.9c7.5,0,13.5-6,13.5-13.5 S235.019,444.2,227.619,444.2z"/> <path d="M450.019,226.1l-85.8-85.8c-5.3-5.3-13.8-5.3-19.1,0c-5.3,5.3-5.3,13.8,0,19.1l62.8,62.8h-273.9c-7.5,0-13.5,6-13.5,13.5 s6,13.5,13.5,13.5h273.9l-62.8,62.8c-5.3,5.3-5.3,13.8,0,19.1c2.6,2.6,6.1,4,9.5,4s6.9-1.3,9.5-4l85.8-85.8 C455.319,239.9,455.319,231.3,450.019,226.1z"/> </g> </g> </g>  
                    </svg>

                    <span class="pb-1"> Abmelden </span>
                  </li>
                </a>
              <ul>
            </div>
          </div>
        </div>
      </div>
    </nav>
  </div>
{/if}

<slot />
