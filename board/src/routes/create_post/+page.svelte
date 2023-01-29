<script lang="ts">
  import { Badge, Form, FormGroup, Input, Label } from "sveltestrap";
  import "bootstrap/dist/css/bootstrap.min.css";
  import { Button } from "sveltestrap";

  let post_title: string;
  let post_body: string;
  let example_user_id = "8ddfeb74-2877-4a0d-8777-f4d7084981b3"

  async function post() {

    const res = await fetch('http://127.0.0.1:8080/api/v1/post/add', {
			method: 'POST',
      headers: {"Content-Type": "application/json"},
			body: JSON.stringify({
				title: post_title,
				content: post_body,
        user_id: example_user_id,
			})
		})
    console.log(res.json())
		return res.json()

  }

</script>

<div class="container">
  <div class="alert alert-dark" role="alert">
    <Form>
      <FormGroup floating label="Titel">
        <Input placeholder="Titel" required bind:value={post_title} />
      </FormGroup>

      <FormGroup>
        <div class="form-floating">
          <textarea
            class="form-control"
            placeholder="Body"
            bind:value={post_body}
            id="floatingTextarea2"
            style="height: 100px"
          />
          <label for="floatingTextarea2">Text</label>
        </div>
      </FormGroup>

      <Button on:click={post}>Post</Button>
    </Form>
  </div>
</div>

<style>
  .container {
    max-width: 700px;
    margin: 0 auto;
    padding: 2rem;
    text-align: center;
  }
  textarea {
    width: 100%;
    height: 300px;
  }

  .container {
    padding: 10px 16px;
    margin: 40px auto;
    max-width: 800px;
  }
</style>
