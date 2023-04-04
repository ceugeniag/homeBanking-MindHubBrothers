const {createApp} = Vue
const app = createApp({
    data(){
        return{
            clients:[],
            firstName: "",
            lastName:"",
            email:"",
        }
    },
    created(){
        this.loadData();
    },
methods: {
    loadData(){
        //Realiza una petición HTTP de tipo GET a la URL /clients con la librería axios.
        axios.get('http://localhost:8080/api/clients')
        //Cuando la petición es respondida se ejecuta el método then
        //El método then guarda en la data de Vue el listado de clientes que llega en el JSON así como el JSON completo. 
        .then(response => {
            this.clients = response.data;
            console.log(this.clients);

        })
        .catch(error=>{
            console.log(error);
        });
    },
    addClient(){
        this.postClient();
        },
    postClient(){
        axios.post('http://localhost:8080/api/clients',{
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email
    })
    .then(response=>{
        this.loadData();
    })
        .catch(error=>{
            console.log(error);
        });
        }
},

})

app.mount("#app")