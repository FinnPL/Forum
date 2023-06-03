<script lang="ts">
    import { goto } from "$app/navigation";
    import { getCookie } from "$lib/functions";
    import { onMount } from "svelte";
    import { default as defaultAvatar } from "../lib/assets/defaultAvatar.png";
    import logoFull  from "../lib/assets/logoFull.png";
    import "../app.css";

    let own_user_id_value: string;
    let cookie_name_value: string;
    let tokenValue: string;
    let avatarSrc: string | null = defaultAvatar;
    let ip: string;
    let search!: HTMLElement;

    search?.addEventListener("keyup", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        document.getElementById("searchButton")?.click();
      }
    });

    async function get_server_ip() {
        ip = "http://" + location.hostname + ":8080/";
    }

    async function initial_load() {
        tokenValue = await getCookie("tokenValue");
        own_user_id_value = await getCookie("userid");
        cookie_name_value = await getCookie("username");
    }

    async function signOut() {
      document.cookie.split(";").forEach(function (c) {
        document.cookie = c
          .replace(/^ +/, "")
          .replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/"); // Sets every cookie as expired to delete them
      });
      await goto("/");
      location.reload();
    }

    async function gotoProfile() {
      const path = window.location.pathname.split("/");
      const userid = path[path.length - 1]; // Get userid from url path
      if (userid != tokenValue) {
        await goto("/profile/" + tokenValue);
        location.reload();
      }
    }

    async function test() {
      console.log("test");
    }

    onMount(async () => {
        await get_server_ip();
        await initial_load();
        if (tokenValue == undefined && location.pathname != "/") {
            await goto("/");
            location.reload();
        } else {  
          const profilePictureRes = await fetch(
          ip + "api/v1/file/profile/" + own_user_id_value,
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
    });
</script>

{#if own_user_id_value != undefined && own_user_id_value != "undefined"}
<div class="sticky top-0 bg-gradient-to-r from-primary to-secondary p-0.5 px-0 pt-0">
  <nav class="flex w-screen items-center max-h-12 p-4 bg-postBG">
      <div class="flex-1 flex justify-start">
        <a href="/">
          <img src={logoFull} alt="Logo" class="h-8 w-auto">
        </a>
      </div>

      <input type="text" class="flex max-w-5xl bg-transparent outline-none !text-bg placeholder-gray-400 rounded-full
       sm:w-1/5 md:w-1/4 lg:w-2/4 xl:w-3/4 2xl:w-5/6" placeholder="Suchen...">
      
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
