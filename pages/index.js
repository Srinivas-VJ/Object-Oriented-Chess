import Head from 'next/head'
import Image from 'next/image'
import LandingPage from "../components/LandingPage"
import styles from '../styles/Home.module.css'


export default function Home() {
  return (
    <div className={styles.container}>
      <Head>
        <title>Chess app</title>
        <meta name="description" content="Generated by create next app" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      
      <main className={styles.main}>


        <LandingPage />
        
      </main>

      
    </div>
  )
}

