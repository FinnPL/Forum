import type { RequestHandler } from "@sveltejs/kit"

export const load = async ({ data, params }: any) => {
	
		const fetchedDataRes = await fetch("http://127.0.0.1:8080/api/v1/post/" +  params.postId)
		const fetchedData = await fetchedDataRes.json()
		let this_post = fetchedData
	
	return {
		params,
		this_post
	}
}

