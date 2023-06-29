<script lang="ts">
  import { token, cookie_name } from "../../lib/Login/login.js";
  import { getCookie } from "../../lib/functions.js";
  import { onMount } from "svelte";
  import Error from "../../lib/Error/error.svelte";
  import { goto } from "$app/navigation";
  import type { Snapshot } from "@sveltejs/kit";
  let post_title: string;
  let post_body: string;
  let post_id: string;
  let tokenValue: string;
  let cookie_name_value: string;
  let error = false;
  let file: any;
  let image_file: any;
  let ip: string;
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
    cookie_name_value = await getCookie("username");
    cookie_name.set(cookie_name_value);
    tokenValue = await getCookie("tokenValue");
    token.set(tokenValue);
  }

  onMount(async () => {
    await get_server_ip();
    await checkLoggedIn();
    await subStores();
  });

  async function subStores() {
    token.subscribe((value: string) => {
      tokenValue = value;
      console.log(tokenValue);
    });

    cookie_name.subscribe((value: string) => {
      cookie_name_value = value;
      console.log(cookie_name_value);
    });
  }

  async function post() {
    error = false;
    const res = await fetch(ip + "api/v1/post", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + tokenValue,
      },
      body: JSON.stringify({
        title: post_title,
        content: post_body,
      }),
    });
    if (!res.ok) {
      error = true;
    }
    const json = await res.json();
    await upload_image(json.id);
    post_id = json.id;
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
        Authorization: "Bearer " + tokenValue,
      },
    });
    console.log(res);
    await goto("/post/" + post_id);
  }
</script>

{#if error}
  <Error />
{/if}

<div class="container mx-auto py-5 max-w-5xl">
  <div class=" px-3 py-3 mb-4 border rounded bg-gray-400 border-gray-500 text-gray-900">
    <form>
      <input class="text-black mb-4 py-1 px-2" placeholder="Titel" required bind:value={post_title} />

      <textarea
        class="w-full py-1 px-2 mb-4 bg-white text-black"
        placeholder="Body"
        bind:value={post_body}
        id="floatingTextarea2"
        style="height: 100px"
      />

      <input type="file" name="file" id="AvatarFile" bind:this={image_file} on:change={handleFileChange}/>

      <div class="flex justify-end">
        <button class="bg-border hover:bg-hover py-2 px-4 rounded-md" on:click={post}>Post</button>
      </div>
    </form>
  </div>
</div>

