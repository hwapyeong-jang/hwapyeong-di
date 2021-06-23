(() => {
    const utils = {
        getUrlParams(search) {
            let hashes = search.slice(search.indexOf('?') + 1).split('&')
            return hashes.reduce((params, hash) => {
                let [key, val] = hash.split('=')
                return Object.assign(params, {[key]: decodeURIComponent(val)})
            }, {})
        }
    }

    const api = {
        getChauffeurs() {
            return axios.get('/chauffeurs', { headers: {'Content-Type': 'application/json'} })
        },
        getChauffeur(id) {
            return axios.get(`/chauffeurs/${id}`, { headers: {'Content-Type': 'application/json'} })
        },
        writeChauffeur(data) {
            return axios.post('/chauffeurs', data, { headers: {'Content-Type': 'application/json'} })
        },
        updateChauffeur(id, data) {
            return axios.put(`/chauffeurs/${id}`, data, { headers: {'Content-Type': 'application/json'} })
        },
        deleteChauffeur(id) {
            return axios.delete(`/chauffeurs/${id}`, { headers: {'Content-Type': 'application/json'} })
        }
    }

    const baseView = {
        async _load() {
            await this._init()
            await this._render()
            this._loadComplete = true
        },
        async _init() {
            this.$el = $(this.el)
            await this.init()
        },
        async _render() {
            this.$el.html(Handlebars.compile(await this.render())(this))
        }
    }

    const views = {
        main :(() => {
            return {
                el: '#container',
                async getChauffeurs() {
                    try {
                        this.chauffeurs = await api.getChauffeurs()
                        this.chauffeurs = this.chauffeurs.data
                    } catch (e) {
                        alert('쇼퍼 목록 호출에 실패하였습니다.')
                    }
                },
                async deleteChauffeur(e) {
                    try {
                        let id = $($(e.target).closest('li')).data('id')
                        await api.deleteChauffeur(id)
                        this.getChauffeurs()
                    } catch (e) {
                        alert('쇼퍼 삭제에 실패하였습니다.')
                    }
                },
                goDetail(e) {
                    let id = $($(e.target).closest('li')).data('id')
                    window.location.hash = 'detail' + (id ? ('?id=' + id) : '')
                },
                init() {
                    this.getChauffeurs()
                    this.$el.on('click', '#goInsert', this.goDetail.bind(this))
                    this.$el.on('click', '.detail_btn', this.goDetail.bind(this))
                    this.$el.on('click', '.delete_btn', this.deleteChauffeur.bind(this))
                },
                render() {
                    return `
                       <div class="entry">
                            <h1>쇼퍼 목록</h1>
                            <div>
                                <button id="goInsert">추가</button>
                            </div>
                            <div class="body">
                              {{#each chauffeurs}}
                              <ul>
                                <li>{{id}}</li>
                                <li>{{name}}</li>
                                <li>{{cellPhone}}</li>
                                <li data-id="{{id}}">
                                    <button class="detail_btn">수정</button>
                                    <button class="delete_btn">삭제</button>
                                </li>
                              </ul>
                              {{/each}}
                            </div>
                        </div>`
                }
            }
        })(),
        detail : (() => {
            return {
                el: '#container',
                async getChauffeur() {
                    try {
                        this.chauffeur = await api.getChauffeur(this.params.id)
                        this.chauffeur = this.chauffeur.data
                    } catch (e) {
                        console.error(e)
                        alert('쇼퍼 호출에 실패하였습니다.')
                    }
                },
                async insertChauffeur() {
                    try {
                        await api.writeChauffeur({
                            name: this.$el.find('#chauffeurName')[0].value,
                            cellPhone: this.$el.find('#chauffeurCellPhone')[0].value
                        })
                        this.goHome()
                    } catch (e) {
                        alert('쇼퍼 추가에 실패하였습니다.')
                    }
                },
                async updateChauffeur() {
                    try {
                        await api.updateChauffeur(this.params.id, {
                            name: this.$el.find('#chauffeurName')[0].value,
                            cellPhone: this.$el.find('#chauffeurCellPhone')[0].value
                        })
                        this.goHome()
                    } catch (e) {
                        console.error(e)
                        alert('쇼퍼 수정에 실패하였습니다.')
                    }
                },
                goHome() {
                    window.location.hash = ''
                },
                init() {
                    this.$el.on('click', '#back', this.goHome.bind(this))
                    this.$el.on('click', '#update', this.updateChauffeur.bind(this))
                    this.$el.on('click', '#insert', this.insertChauffeur.bind(this))
                    this.params = utils.getUrlParams(window.location.hash)
                    if(this.params.id) {
                        this.getChauffeur(this.params.id)
                    }
                },
                render() {
                    return `
                    <div class="entry">
                        <h1>쇼퍼 상세</h1>
                        <div>
                            <button id="back">돌아가기</button>
                            {{#if params.id}}
                            <button id="update">수정</button>
                            {{else}}
                            <button id="insert">추가</button>
                            {{/if}}
                        </div>
                        <div class="body">
                            <ul><input type="text" id="chauffeurName" value="{{chauffeur.name}}"></ul>
                            <ul><input type="text" id="chauffeurCellPhone" value="{{chauffeur.cellPhone}}"></ul>
                        </div>
                    </div>
                    `
                }
            }
        })()
    }

    const router = (hash) => {
        if(hash === '' || hash === '#') {
            return views.main
        } else if (hash.includes('#detail')) {
            return views.detail
        }
    }

    const proxy = (route) => {
        let assign = Object.assign({}, route, baseView)
        return new Proxy(assign, {
            set(target, key, value) {
                target[key] = value
                if(assign._loadComplete) {
                    assign._render()
                }
            }
        })
    }

    const start = () => {
        if(this._view) {
            this._view.$el.off()
        }
        this._view = proxy(router(window.location.hash))
        this._view._load()
    }
    window.addEventListener("hashchange", start, false)
    start()
})()