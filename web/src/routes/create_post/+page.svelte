<script lang="ts">
  import { getCookie } from "../../lib/functions.js";
  import { onMount } from "svelte";
  import Error from "../../lib/Error/error.svelte";
  import { goto } from "$app/navigation";
  import type { Snapshot } from "@sveltejs/kit";
  import { fetcher } from "../../lib/functions.js";
  import { store_token, store_username } from "$lib/stores.js";
  let post_title: string;
  let post_body: string;
  let post_id: string;
  let error = false;
  let file: any;
  let image_file: File;
  let ip: string;
  let buttonPressed = false;
  $: saved_data = { post_title, post_body };

  export const snapshot: Snapshot = {
    capture: () => saved_data,
    restore: (value) => (
      (post_title = value.post_title), (post_body = value.post_body)
    ),
  };

  async function get_server_ip() {
    ip = "http://" + location.hostname + ":8080/";
  }

  async function checkLoggedIn() {
    $store_username = await getCookie("username");
    $store_token = await getCookie("tokenValue");

  }

  onMount(async () => {
    await get_server_ip();
    await checkLoggedIn();
  });

  

  async function post() {
    buttonPressed = true;
    error = false;
    if(post_title == null || post_body == null) {
      return;
    }
    const res = await fetcher("api/v1/post", "POST",{
        title: post_title,
        content: post_body,
      })

    await upload_image(res.id);
    post_id = res.id;
    if(!res.ok) {
      buttonPressed = false;
      error = true;
    }
    return res.json();
  }

  const handleFileChange = (event: any) => {
    file = event.target.files[0];
  };

  async function upload_image(post_id: string) {
    const formData = new FormData();
    formData.append("file", file);
    const res = await fetch(ip + "api/v1/file/post/" + post_id, {
      method: "POST",
      body: formData,
      headers: {
        Authorization: "Bearer " + $store_token,
      },
    });
    await goto("/post/" + post_id);
  }
</script>

{#if error}
  <Error />
{/if}

<div class="container mx-auto py-5 max-w-5xl">
  <div class="bg-postBG border border-border p-4 rounded-lg max-w-5xl">
    <form>
      <div class="text-lg font-semibold mb-2">Titel:</div>
      <div class="mb-6">
        <textarea class="text-white bg-ui border border-border rounded-lg w-full resize-none" maxlength="255" bind:value={post_title}/>
      </div>

      <div class="text-lg font-semibold mb-2">Inhalt:</div>
      <div class="mb-6">
        <textarea class="text-white bg-ui border border-border rounded-lg w-full" bind:value={post_body}/>
      </div>

      <hr class="h-0.5 border-t-0 bg-text" />

      <div class="text-lg font-semibold pt-3 mb-2">Bild:</div>
      <div class="mb-4">
        <input type="file" name="file" id="AvatarFile" bind:this={image_file} on:change={handleFileChange}/>
      </div>


      <div class="flex justify-end">
        {#if buttonPressed === false}
          <button class="bg-border hover:bg-hover py-2 px-4 rounded-md" on:click={post}>Post</button>
        {:else}
          <button class="bg-border hover:bg-hover py-2 px-4 rounded-md" disabled>Post</button>
        {/if}
      </div>
    </form>
  </div>
</div>




  


