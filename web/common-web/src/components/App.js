import React, { Component } from 'react';
import {connect} from 'react-redux';
// import PropTypes from 'prop-types';

import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
// import {
//   Redirect,
//   withRouter
// } from 'react-router-dom';
import { withRouter } from 'react-router'

import Header from './common/Header';
import Footer from './common/Footer';
// import PropertyTaxSearch from "./contents/PropertyTaxSearch";

import router from "../router";

//api import
// import api from "../api/commonAPIS"


class App extends Component {

  // constructor(props) {
  //     super(props);
  //     // this.getOtp = this.getOtp.bind(this);
  //     // this.validateOtp = this.validateOtp.bind(this);
  //     // this.callLogin = this.callLogin.bind(this);
  // }

  componentWillReceiveProps(nextProps) {
      if (nextProps.redirectTo) {
          this.props.history.replace(nextProps.redirectTo);
          this.props.onRedirect();
      }
  }

  componentWillMount() {
    let {history}=this.props;
    // let commonState=JSON.parse(window.localStorage.getItem("reduxPersist:common"));
    // console.log(commonState);
    if (window.localStorage.getItem("token")) {
        history.push("/dashboard");
    } else {
        history.push("/");
    }

      // this.props.setLabels(agent.labels.getLabels());
      // const token = window.localStorage.getItem('jwt');
      // const userId = window.localStorage.getItem('userId');
      // const type = window.localStorage.getItem('type');
      //
      // let currentUser = window.localStorage.getItem('currentUser');
      //
      // if(currentUser) {
      //     currentUser = JSON.parse(currentUser);
      // }
      //
      // if (token) {
      //     // agent.setToken(token);
      //     // agent.setUserId(userId);
      //     // agent.setType(type);
      // }

      // api.commonApiPost("user/oauth/token",{tenantId:"default",
      //     username:"narasappa",
      //     password:"demo",
      //     grant_type:"password",
      //     scope:"read"}).then((response)=>{
      //       console.log(response);
      //     },(err)=> {
      //     console.log(err);
      //   });



      // this.props.onLoad(!currentUser
      //     ? agent.Auth.login((this.props.auth.userName || "9999999999"), (this.props.auth.password || "demo"))
      //     : {UserRequest: currentUser}, token);
  }

  componentDidMount()
  {


  }

  render() {
    // console.log(this);

    var {toggleDailogAndSetText,isDialogOpen,msg,token,history}=this.props;

    const actions = [
      <FlatButton
        label="Ok"
        primary={true}
        onTouchTap={this.handleClose}
      />,
    ];
    return (
      <div className="App">
          <Header/>
              {router}
          <Footer/>

          <Dialog
            actions={actions}
            modal={false}
            open={isDialogOpen}
            onRequestClose={toggleDailogAndSetText(false,"")}
            >
            {msg}
          </Dialog>




      </div>
    );
  }
}

const mapStateToProps = state => ({
    // labels: state.labels,
    // appLoaded: state.common.appLoaded,
    // appName: state.common.appName,
    // currentUser: state.common.currentUser,
    redirectTo: state.common.redirectTo,
    token:state.common.token,
    // pleaseWait: state.common.pleaseWait,
    // token:state.common.token,
    isDialogOpen: state.form.dialogOpen,
    msg: state.form.msg
});

// this.props.appLoaded

const mapDispatchToProps = dispatch => ({
    // onLoad: (payload, token) => dispatch({type: 'APP_LOAD', payload, token, skipTracking: true}),
    onRedirect: () => dispatch({type: 'REDIRECT'}),
    // setLabels: payload => dispatch({type: 'LABELS', payload}),
    // onUpdateAuth: (value, key) => dispatch({type: 'UPDATE_FIELD_AUTH', key, value}),
    // onLogin: (username, password) => {
    //     dispatch({
    //         type: 'LOGIN',
    //         payload: []//agent.Auth.login(username, password)
    //     })
    // },
    // updateError: (error) =>
    //     dispatch({
    //         type: 'UPDATE_ERROR',
    //         error
    //     }),
    // setPleaseWait: (pleaseWait) =>
    //     dispatch({
    //         type: 'PLEASE_WAIT',
    //         pleaseWait
    //     }),
   toggleDailogAndSetText: (dailogState,msg) => {
          dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
        }
});


// App.contextTypes = {
//     router: React.PropTypes.object.isRequired
// };




export default withRouter(connect(mapStateToProps, mapDispatchToProps)(App));