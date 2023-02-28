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
  import { own_user_id, token } from "../lib/Login/login";
  let own_user_id_value: string;

  own_user_id.subscribe((value: string) => {
    own_user_id_value = value;
  });

  function signOut() {
    console.log("test");
    document.cookie = "tokenValue=undefined";
    document.cookie = "username=undefined";
  }
</script>

<Navbar color="light" light expand="md">
  <NavbarBrand href="/">GHSE-Board</NavbarBrand>
  <Nav class="ms-auto">
    {#if true}
      <NavItem>
        <NavLink href={"/profile/" + own_user_id_value}>Profile</NavLink>
      </NavItem>
      <NavItem>
        <NavLink href="/create_post">Create New Post</NavLink>
      </NavItem>
    {/if}
    <NavItem>
      <NavLink href="/search">Search</NavLink>
    </NavItem>
    <NavItem>
      {#if "currentUser"}
        <Button on:click={signOut}>Sign Out</Button>
      {/if}
    </NavItem>
  </Nav>
</Navbar>

<slot />
