import { useState } from "react";
import * as React from 'react';
import Button from 'react-bootstrap/Button';
import "bootstrap/dist/css/bootstrap.min.css"
import Form from 'react-bootstrap/Form';


const NewPost = () => {
    const[title, setTitle] = useState("");
    const[body, setBody] = useState("");
    const[author, setAuthor] = useState("Sueer");

    const handlePost = (e) => {
        e.preventDefault()
        const post = {title, body, author};

        fetch("http://localhost:8000/posts", {
            method: "POST",
            headers: {"Content-Type" : "application/json" },
            body: JSON.stringify(post)
    }).then(() => {
        console.log("New Post added")
    })

    }

    return (
        <Form onSubmit={handlePost}>
      <Form.Group className="mb-3" controlId="post-title">
        <Form.Control size="lg "type="text" placeholder="Titel" 
        required
        value={title}
        onChange={(e)=> setTitle(e.target.value)}
        />    
      </Form.Group>

      <Form.Group className="mb-3" controlId="post-body">
        <Form.Label>Post Text</Form.Label>
        <Form.Control as="textarea" rows={3} 
         required
         value={body}
         onChange={(e)=> setBody(e.target.value)}
         />   
        </Form.Group>     

      
      <Form.Group className="mb-3" controlId="post-author">
        <Form.Label>Dein Name:</Form.Label>
        <Form.Control size="lg "type="text" placeholder="Name" 
        required
        value={author}
        onChange={(e)=> setAuthor(e.target.value)}
        />

      </Form.Group>

      <Button variant="primary" type="submit"> Submit </Button>
      <br></br>
      <p>{title}</p>
      <p>{body}</p>
      <p>{author}</p>
    </Form>
    );
}

export default NewPost;