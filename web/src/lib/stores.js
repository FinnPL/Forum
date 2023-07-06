import { writable } from "svelte/store";

/* The code above does the following:
1. Creates a writable store called "store */

/* Example code:
if ($store_token === undefined) {
    await goto("/login");
} */

export const store_token = writable(undefined);
export const store_username = writable(undefined);
export const store_userid = writable(undefined);
