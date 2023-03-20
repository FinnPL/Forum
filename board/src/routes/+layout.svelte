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
  import { own_user_id } from "../lib/Login/login";
  let own_user_id_value: string;

  own_user_id.subscribe((value: string) => {
    own_user_id_value = value;
  });

  function signOut() {
    console.log("test");
    document.cookie = "tokenValue=undefined";
    document.cookie = "username=undefined";
    document.cookie = "userid=undefined"
  }
</script>


<Navbar  >
  <NavbarBrand href="/">GHSE-Board</NavbarBrand>
  
  <Nav class="ms-auto">
    {#if own_user_id_value != undefined && own_user_id_value != "undefined"}
      <NavItem>
        <NavLink href={"/profile/" + own_user_id_value}>Profil</NavLink>
      </NavItem>
      <NavItem>
        <NavLink href="/create_post">Post erstellen</NavLink>
      </NavItem>
    <NavItem>
      <NavLink href="/search">Suche</NavLink>
    </NavItem>
    <NavItem>
        <Button color="primary" on:click={signOut} on:click={() => location.reload()}>Abmelden</Button>
    </NavItem>
  {/if}
  </Nav>
</Navbar>

<slot />
