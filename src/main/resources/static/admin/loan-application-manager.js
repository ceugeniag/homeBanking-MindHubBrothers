const {createApp} = Vue

const app = createApp ({
    data(){
        return{
            client:[],
            clients:[],
            loanId:0,
            maxAmount:0,
            payments:[],
            interest:0,
            name:" ",
            checked:[]
        }
    },
    created(){
    },
    methods:{
        // loadData(){
        //     axios.get('/api/clients/current')
        //     .then(response => {
        //         this.client = response.data;
        //         console.log(this.client);
        //     })
        //     .catch(error=>{
        //         console.log(error);
        //     });
        // },
        logout(){
            axios.post('/api/logout')
            .then(response=> console.log('signed out!!!'), (window.location.href = '/web/index.html'))
            .catch(error => console.log(error))
        },
        // loansData(){
        //     axios.get('/api/loans')
        //     .then(response => {
        //         console.log("A ver si entró")
        //         this.loans = response.data
        //         console.log(this.loans)
        //         })
        //     .catch(error=> console.log(error))
        // },
        loanApply() {
            console.log(this.name)
            console.log(this.maxAmount)
            console.log(this.checked);
            console.log(this.interest)

            Swal.fire({
            title: ' Apply loan',
            text: 'You are about to apply for a new loan, are you sure?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Apply',
            cancelButtonText: 'Cancel'
            }).then((result) => {
            if (result.isConfirmed) {
                axios.post('/api/loans/manager', {
                name: this.name,
                maxAmount: this.maxAmount,
                payments: this.checked,
                interest: this.interest,
                })
                .then(response =>{
                    if (response.status == "201"){
                        this.loanApply = true,
                    
                Swal.fire({
                                    icon: 'success',
                                    title: 'You have a new Loan!',
                                    showCancelButton: true,
                                    confirmButtonText: 'Accepted',
                                    cancelButtonText: 'Cancel',
                                    timer: 6000,
                                })
                    (window.location.href = 'manager.html')
                            }
                })
                .catch(error => 
                    console.log(error),
                    Swal.fire({
                icon: 'error',
                text: error.response.data,
                }))
            }
            })
        },
    },
    })
app.mount("#app")