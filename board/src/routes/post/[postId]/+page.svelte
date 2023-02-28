<script lang="ts">
  import { Form, FormGroup } from "sveltestrap";
  import "bootstrap/dist/css/bootstrap.min.css";
  import { Button } from "sveltestrap";
  import { onMount } from "svelte";
  export let data: any

  console.log(data)


  let userID: string
  let comment_text: string
  let comment_list: any = []
  let page: number = 0
  
  let title = data.this_post.title
  let content = data.this_post.content
  let date = data.this_post.date
  let user_name = data.this_post.user_name
  let thisID:string = data.this_post.id

  //Provisorisch
  let example_user_id = "8ddfeb74-2877-4a0d-8777-f4d7084981b3"
  let example_title = "Ein Titel von einem Kommentar"

  async function getFirstComments() {
    const fetchedRes = await fetch("http://127.0.0.1:8080/api/v1/comment/get/" + thisID +"/" + page)
    const fetchedData = await fetchedRes.json()

    comment_list = fetchedData
  }

  async function getComments() {
    const fetchedRes = await fetch("http://127.0.0.1:8080/api/v1/comment/get/" + thisID +"/" + page)
    const fetchedData = await fetchedRes.json()

    comment_list = comment_list.concat(fetchedData)
    
  }


  getFirstComments()
  
  async function post_comment() {
    const res = await fetch('http://127.0.0.1:8080/api/v1/comment/add/', {
			method: 'POST',
      headers: {"Content-Type": "application/json"},
			body: JSON.stringify({
				title: example_title,
				text: comment_text,
        user_id: example_user_id,
        post_id: thisID
			})
		})
    console.log(res.json())
		return res.json()

  }

  onMount(async () => {
    window.onscroll = function (ev) {
      if (
        window.innerHeight + window.pageYOffset >=
        document.body.offsetHeight
      ) {
        page += 1;
        getComments();
      }
    };
  });



</script>

<div class="container">
  <div class="alert alert-dark">
    <h2>Title: {title}</h2>
    <p1>Body: {content}</p1><br />
    <br />
    <p2>Created: {date}</p2><br />
    <br />
    <p><a href={"/profile/" + userID}>Author: {user_name}</a></p>
    <br />

    <Form method="POST">
      <FormGroup>
        <h3>Comment:</h3>
        <div class="form-floating">
          <textarea
            class="form-control"
            placeholder="Body"
            bind:value={comment_text}
            id="comment_text"
            name="comment_text"
            style="height: 100px"
          />
          <label for="floatingTextarea2">Text</label>
        </div>
      </FormGroup>
      <div class="button">
        <Button on:click={post_comment}>Post Comment</Button>
      </div>
    </Form>
  </div>

  {#each comment_list as comment (comment.id)}
    <div class="alert alert-dark">
      <h3>Title: {comment.title}</h3>
      <p2>Body: {comment.content}</p2><br />
      <br />
      <p>
        <a href={"/profile/" + comment.user_id}>Author: { comment.user_name }</a>

      </p>
    </div>
  {/each}
</div>

<style>
  .container {
    padding: 10px 16px;
    margin: 40px auto;
    max-width: 800px;
  }
  .container h2 {
    font-size: 30px;
    color: #3538f1d0;
    margin-bottom: 8px;
  }
  .container p1 {
    font-size: 20px;
  }
  .container a {
    text-decoration: none;
    color: inherit;
  }
  .button {
    text-align: center;
  }
</style>