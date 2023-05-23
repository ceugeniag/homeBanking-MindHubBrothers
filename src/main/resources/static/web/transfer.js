const {createApp} = Vue

const app = createApp ({
    data(){
        return{
            clients:[],
            accounts:[],
            valorID :(new URLSearchParams(location.search)).get("id"),
            isOwnAccount: false,
            amount: '',
            description: '',
            numberAccountFrom:'',
            numberAccountTo:'',

        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get('/api/clients/current')
            .then(response => {
                this.clients = response.data;
                console.log(this.clients);
                this.accounts = this.clients.accounts;
                console.log(this.accounts);


            })
            .catch(error=>{
                console.log(error);
            });
        },
        logout(){
            axios.post('/api/logout'),{
            headers: { 'content-type': 'application/x-www-form-urlencoded'}}
            .then(response=> console.log('signed out!!!'), (window.location.href = '/web/index.html'))
            .catch(error => console.log(error))
        },
        transfer(){
            axios
            .post('/api/transactions', `amount=${this.amount}&description=${this.description}&numberAccountFrom=${this.numberAccountFrom}&numberAccountTo=${this.numberAccountTo}`, {
                headers: { 'content-type': 'application/x-www-form-urlencoded' }
            })
            .then(response => console.log('transfer'), (window.location.href = '/web/account.html'))
            .catch(error => console.log(error))
        },

    },
    
    })
app.mount("#app")
// 
