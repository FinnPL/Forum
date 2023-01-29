import { useState, useEffect } from 'react'
import PostList from './PostList'
import * as React from 'react'

const Home = () => {
  const [posts, setPosts] = useState(null)

  useEffect(() => {
    fetch('http://localhost:8000/posts')
      .then((res) => {
        return res.json()
      })
      .then((data) => {
        setPosts(data)
      })
  }, [])

  return (
    <div className='home'>
      {posts && <PostList posts={posts} title='Alle Posts!' />}
    </div>
  )
}
export default Home
