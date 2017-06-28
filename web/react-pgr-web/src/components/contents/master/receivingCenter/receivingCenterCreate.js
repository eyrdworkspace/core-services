import React, {Component} from 'react';
import {connect} from 'react-redux';
import SimpleMap from '../../../common/GoogleMaps.js';

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import Checkbox from 'material-ui/Checkbox';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import Api from '../../../../api/api';

var flag = 0;
const styles = {
  headerStyle : {
    color: 'rgb(90, 62, 27)',
    fontSize : 19
  },
  marginStyle:{
    margin: '15px'
  },
  paddingStyle:{
    padding: '15px'
  },
  errorStyle: {
    color: red500
  },
  underlineStyle: {
    borderColor: brown500
  },
  underlineFocusStyle: {
    borderColor: brown500
  },
  floatingLabelStyle: {
    color: brown500
  },
  floatingLabelFocusStyle: {
    color: brown500
  },
  customWidth: {
    width:100
  },
  checkbox: {
    marginTop: 37
  }
};

var _this;

class CreateReceivingCenter extends Component {
    constructor(props) {
      super(props);
      this.state = {
          id:'',
          data:'',
          open:false
         }
    }

    componentDidMount() {
        if(this.props.match.params.id) {

            this.setState({
              id:this.props.match.params.id,
            });
            var body = {}
            let  current = this;
            let {setForm} = this.props;

            Api.commonApiPost("/pgr-master/receivingcenter/_search",{id:this.props.match.params.id},body).then(function(response){
                console.log(response);
                current.setState({data:response.ReceivingCenterType})
                setForm(response.ReceivingCenterType[0])
            }).catch((error)=>{
                console.log(error);
            })
        } else {
          let {initForm}=this.props;
          initForm();
        }
    }



    componentDidUpdate() {

    }

    submitForm = (e) => {

      e.preventDefault()

      var current = this;

      var body = {
          "ReceivingCenterType":{
            "id": this.props.createReceivingCenter.id,
           "name" :this.props.createReceivingCenter.name,
           "code" :this.props.createReceivingCenter.code,
           "description" :this.props.createReceivingCenter.description,
           "active" : !this.props.createReceivingCenter.active ? false : this.props.createReceivingCenter.active,
           "iscrnrequired" : !this.props.createReceivingCenter.iscrnrequired ? false : this.props.createReceivingCenter.iscrnrequired,
           "orderno" :this.props.createReceivingCenter.orderno,
           "tenantId":"default"
          }
      }

      if(this.props.match.params.id){
          Api.commonApiPost("/pgr-master/receivingcenter/"+body.ReceivingCenterType.code+"/_update",{},body).then(function(response){
              console.log(response);
              current.setState({
                open: true
              });
          }).catch((error)=>{
              console.log(error);
          })
      } else {
          Api.commonApiPost("/pgr-master/receivingcenter/_create",{},body).then(function(response){
              console.log(response);
              current.setState({
                open: true
              })
              current.props.resetObject('createReceivingCenter');
          }).catch((error)=>{
              console.log(error);
          })
      }
    }

  handleClose = () => {
    this.setState({open: false});
  };

    render() {

      let {
        createReceivingCenter ,
        fieldErrors,
        isFormValid,
        isTableShow,
        handleUpload,
        files,
        handleChange,
        handleMap,
        handleChangeNextOne,
        handleChangeNextTwo,
        buttonText
      } = this.props;


      let {submitForm} = this;

      console.log(createReceivingCenter);

      return(
        <div className="createReceivingCenter">
          <form autoComplete="off" onSubmit={(e) => {submitForm(e)}}>
              <Card style={styles.marginStyle}>
                  <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Contact Information < /div>} />
                  <CardText style={{padding:0}}>
                      <Grid>
                          <Row>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Name"
                                      value={createReceivingCenter.name? createReceivingCenter.name : ""}
                                      errorText={fieldErrors.name ? fieldErrors.name : ""}
                                      onChange={(e) => handleChange(e, "name", true, '')}
                                      id="name"
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Code"
                                      value={createReceivingCenter.code? createReceivingCenter.code : ""}
                                      errorText={fieldErrors.code ? fieldErrors.code : ""}
                                      onChange={(e) => handleChange(e, "code", true, '')}
                                      id="code"
                                      disabled={this.state.id ? true : false }
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Description"
                                      value={createReceivingCenter.description? createReceivingCenter.description : ""}
                                      errorText={fieldErrors.description ? fieldErrors.description : ""}
                                      onChange={(e) => handleChange(e, "description", false, '')}
                                      multiLine={true}
                                      id="description"
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                              {console.log(createReceivingCenter.active)}
                                  <Checkbox
                                    label="Active"
                                    style={styles.checkbox}
                                    checked = {createReceivingCenter.active || false}
                                    onCheck = {(e, i, v) => { console.log(createReceivingCenter.active, i);

                                      var e = {
                                        target: {
                                          value:i
                                        }
                                      }
                                      handleChange(e, "active", false, '')
                                    }}
                                    id="active"
                                  />
                              </Col>
                              <div className="clearfix"></div>
                              <Col xs={12} md={3} sm={6}>
                                  <Checkbox
                                    label="CRN"
                                    style={styles.checkbox}
                                    checked ={createReceivingCenter.iscrnrequired}
                                    onCheck = {(e, i, v) => {
                                      var e = {
                                        target: {
                                          value:i
                                        }
                                      }
                                      handleChange(e, "iscrnrequired", false, '')
                                    }}
                                    id="iscrnrequired"
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Order No"
                                      value={createReceivingCenter.orderno ? createReceivingCenter.orderno : ""}
                                      errorText={fieldErrors.orderno ? fieldErrors.orderno : ""}
                                      onChange={(e) => handleChange(e, "orderno", true, '')}
                                      id="orderno"
                                  />
                              </Col>

                          </Row>
                      </Grid>
                  </CardText>
              </Card>
              <div style={{textAlign:'center'}}>
                <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={!isFormValid} label={this.state.id != '' ? 'Update' : 'Create'} backgroundColor={"#5a3e1b"} labelColor={white}/>
                <RaisedButton style={{margin:'15px 5px'}} label="Close"/>
              </div>
          </form>
          <Dialog
               title="Data Added Successfully"
               actions={<FlatButton
   				        label="Close"
   				        primary={true}
   				        onTouchTap={this.handleClose}
   				      />}
               modal={false}
               open={this.state.open}
               onRequestClose={this.handleClose}
             >
              Data Added Successfully
         </Dialog>
        </div>)
    }

}

const mapStateToProps = state => {
  return ({createReceivingCenter : state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["name","code","orderno"]
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },

  setForm: (data) => {
    dispatch({
      type: "SET_FORM",
      data,
      isFormValid:true,
      fieldErrors: {},
      validationData: {
        required: {
          current: ["name","code","orderno"],
          required: ["name","code","orderno"]
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },

  resetObject: (object) => {
    console.log(object);
   dispatch({
     type: "RESET_OBJECT",
     object
   })
  },

  handleChange: (e, property, isRequired, pattern) => {
    console.log("handlechange"+e+property+isRequired+pattern);
    dispatch({
      type: "HANDLE_CHANGE",
      property,
      value: e.target.value,
      isRequired,
      pattern
    });
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(CreateReceivingCenter);