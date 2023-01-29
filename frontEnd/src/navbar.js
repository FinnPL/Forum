import * as React from 'react'
import Nav from 'react-bootstrap/Nav'
import 'bootstrap/dist/css/bootstrap.min.css'
import Container from 'react-bootstrap/Container'
import Navbar from 'react-bootstrap/Navbar'
import NavbarBrand from 'react-bootstrap/esm/NavbarBrand'

const Navibar = () => {
  return (
    <Navbar bg='primary' variant='dark'>
      <Container>
        <NavbarBrand href='/'>GHSE-Board</NavbarBrand>
        <Nav className='ms-auto'>
          <Nav.Link href='/profile'>Profile</Nav.Link>
          <Nav.Link href='/create'>Neuer Post</Nav.Link>
        </Nav>
      </Container>
    </Navbar>
  )
}

export default Navibar
