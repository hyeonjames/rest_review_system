import $ from 'jquery'
import q from 'q'
let host = ''
let crossDomain = false
let xhrFields = null


let com = {
    get (url) {
        let p = q.defer();
        $.ajax({
            method : 'GET',
            url : host + url,
            crossDomain: crossDomain,
            xhrFields: xhrFields,
            dataType : 'json'
        }).done((data)=>{
            if(data.error) {
                alert(data.data);
                p.reject(data.data)
            } else {
                p.resolve(data.data)
            }
        })
        return p.promise;
    },
    get2 (url) {
        let p = q.defer();
        $.ajax({
            method : 'GET',
            url : host + url,
            crossDomain: crossDomain,
            xhrFields: xhrFields,
            dataType : 'json'
        }).done((data)=>{
            if(data.error) {
                p.reject(data.data)
            } else {
                p.resolve(data.data)
            }
        })
        return p.promise;
    },
    post (url, data) {
        let p = q.defer();
        $.ajax({
            method : 'POST',
            url : host + url,
            crossDomain: crossDomain,
            xhrFields: xhrFields,
            data : data,
            dataType : 'json'
        }).done((data)=>{
            if(data.error) {
                alert(data.data);
                p.reject(data.data)
            } else {
                p.resolve(data.data)
            }
        })
        return p.promise;
    },
    getCoord(address, cb, errCb) {
        let daum = window.daum
        let geocoder = new daum.maps.services.Geocoder()
        geocoder.addressSearch(address, (result,status)=> {
            if(status == daum.maps.services.Status.OK) {
                cb( {
                    x : result[0].x,
                    y : result[0].y
                })
            }
            else if(status == daum.maps.services.Status.ZERO_RESULT) {
                if(errCb) {
                    errCb();
                }
            }
        })
    },
    getAddress(cb) {
        let daum = window.daum
        new daum.Postcode({
            oncomplete : function(data) {
                cb(data)
                
            }
        }).open()
    },
    log() {
        let con = console
        con.log.apply(this, arguments)
    },
    categories() {
        return [
            {cateNo : 1, name : '한식'},
            {cateNo : 2, name : '중식'},
            {cateNo : 3, name : '일식'},
            {cateNo : 4 , name : '양식'},
            {cateNo : 6, name : '기타'}
        ]
    },
    session: {
        cached : null, 
        info(update = false) {
            let p = q.defer()
            if(!update) {
                if (com.session.cached != null) {
                    p.resolve(com.session.cached)
                }
            }
            com.get('/api/auth/info')
            .then((data)=> {
                com.session.cached = data
                p.resolve(data)
            }, p.reject)
            return p.promise
        },
        main() {
            
            let page = {
                M : '/member/popular/0/1',
                O : '/owner/manage',
                A : '/admin/review/1',
                G : '/'
            };
            let p = q.defer();
            if(com.session.cached) {

                p.resolve(page[com.session.cached.type])
            } else {

                com.get2('/api/auth/info')
                .then((r)=>{
                    com.session.cached = r;
                    p.resolve(page[r.type])
                }, ()=>{
                    p.resolve(page['G']);
                })
            }
            return p.promise;
        },
        expire () {
            return com.get('/api/auth/logout')
            .then(()=>{
                com.session.cached = '';
            })
        }
    },
    upload(file) {
        let frm = new FormData();
        frm.append('uploadFile', file);
        let p = q.defer();
        $.ajax({
            url : host + 'api/file/image/upload',
            processData : false,
            contentType : false,
            data: frm,
            crossDomain: crossDomain,
            xhrFields: xhrFields,
            type : 'POST',
            success :  (data)=>{
                if(data.error) {
                    alert(data.data);
                } else {
                    p.resolve(data.data);
                }
            }
        })
        return p.promise;
    }
}

export default com