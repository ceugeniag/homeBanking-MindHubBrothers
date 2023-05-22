const {createApp} = Vue
const app = createApp ({
    data(){
        return{
            clients:[],
            account:[],
            transactions:[],
            valorID :(new URLSearchParams(location.search)).get("id"),
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get('http://localhost:8080/api/clients/current/accounts/'+ this.valorID)
            .then(response => {
                this.account = response.data;
                console.log(this.account);
                this.transactions= this.account.transactions;
                console.log(this.transactions);
            })
            .catch(error=>{
                console.log(error);
            });
        },
        logout(){
            axios.post('/api/logout')
            .then(response=> console.log('signed out!!!'), (window.location.href = '/web/index.html'))
            .catch(error => console.log(error));
        },
        // filteredTransactions() {
        //     let today = new Date().toISOString().slice(0, 10);
        //     return this.account.transactions
        //     .filter(transaction => transaction.transactionDate && transaction.transactionDate.slice(0, 10) <= today)
        //     .filter(transaction => transaction.transactionDate.slice(0, 10) <= today)
        //         .sort((a, b) => new Date(b.transactionDate) - new Date(a.transactionDate));
        // }
        filteredTransactions() {
            if (this.transactions && Array.isArray(this.transactions)) {
                const today = new Date().toISOString().slice(0, 10);
                return this.transactions.filter(transaction => transaction.transactionDate && transaction.transactionDate.slice(0, 10) <= today);
            } else {
                return [];
            }
        }
        
    },
    })
app.mount("#app")