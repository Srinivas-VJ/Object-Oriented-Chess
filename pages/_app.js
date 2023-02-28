import '../styles/globals.css'
import Navbar from '../components/Navbar.js'

function MyApp({ Component, pageProps }) {
  return <>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.6.1/sockjs.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
  <Navbar / >
  <Component {...pageProps}/>
  </>
}

export default MyApp
