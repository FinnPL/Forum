<script lang="ts">
  import "bootstrap/dist/css/bootstrap.min.css";

  import {
    Collapse,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    Dropdown,
    DropdownToggle,
    DropdownMenu,
    DropdownItem,
    Container,
    Button,
  } from "sveltestrap";

  function signOut() {
    console.log("test");
  }
</script>

<Navbar color="light" light expand="md">
  <NavbarBrand href="/">GHSE-Board</NavbarBrand>
  <Nav class="ms-auto">
    {#if true}
      <NavItem>
        <NavLink href={"/profile/" + "$currentUser.id"}>Profile</NavLink>
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
