<script lang="ts">
  import "bootstrap/dist/css/bootstrap.min.css";
  import {
    Navbar,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    Button,
  } from "sveltestrap";
  import { goto } from "$app/navigation";
  import { getCookie } from "$lib/functions";
  import { onMount } from "svelte";
  let own_user_id_value: string;
  let tokenValue: string;

  async function initial_load() {
    tokenValue = await getCookie("tokenValue");
    own_user_id_value = await getCookie("userid");
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
  onMount(async () => {
    await initial_load();
    if (tokenValue == undefined && location.pathname != "/") {
      await goto("/");
      location.reload();
    }
  });
</script>

<Navbar>
  <NavbarBrand href="/">GHSE-Board</NavbarBrand>

  <Nav class="ms-auto">
    {#if own_user_id_value != undefined && own_user_id_value != "undefined"}
      <NavItem>
        <NavLink href={"/profile/" + own_user_id_value} on:click={gotoProfile}
          >Profil</NavLink
        >
      </NavItem>
      <NavItem>
        <NavLink href="/create_post">Post erstellen</NavLink>
      </NavItem>
      <NavItem>
        <NavLink href="/search">Suche</NavLink>
      </NavItem>
      <NavItem>
        <Button color="primary" on:click={signOut}>Abmelden</Button>
      </NavItem>
    {/if}
  </Nav>
</Navbar>

<slot />
