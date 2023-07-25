import React, {Component} from 'react';
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'

import './Logging.css'
import LoggingAlert from './LoggingAlert';

class Logging extends Component{

    constructor(props){
        super(props);
        this.LoggingAlert = React.createRef();
    }

    handleSubmit = event => {
        event.preventDefault();
        console.log(event.target.username.value);
        console.log(event.target.password.value);
        this.logUser(event.target.username.value, event.target.password.value);
    }

    logUser(username, password){
        fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password,
            })
        }).then(function(response){
                if(response.status === 200){
                    this.showLoggingAlert("success", "User logged in!", "You have been logged in successfully");
                } else if (response.status === 422){
                    this.showLoggingAlert("danger", "Invalid input", "Please try again");
                } else {
                    this.showLoggingAlert("danger", "Invalid username and/or password", "Please try again");
                }
            }.bind(this)).catch(function(error){
                this.showLoggingAlert("danger", "Error!", "Something went wrong");
            }.bind(this));
    }

    showLoggingAlert(variant, heading, message){
        this.LoggingAlert.current.setVariant(variant);
        this.LoggingAlert.current.setHeading(heading);
        this.LoggingAlert.current.setMessage(message);
        this.LoggingAlert.current.setVisible(true);
    }


  render() {
    return (
    <>
        <div className="Logging">
            <Form onSubmit={this.handleSubmit}>
                <Form.Group controlId="username" size="lg">
                    <Form.Label>Username</Form.Label>
                    <Form.Control autoFocus name="username"/>
                </Form.Group>

                <Form.Group controlId="password" size="lg">
                    <Form.Label>Password</Form.Label>
                    <Form.Control type="password" name="password"/>
                </Form.Group>

                <Button block size = "lg" type = "submit">Login</Button>
            </Form>
        </div>

    <LoggingAlert ref={this.LoggingAlert}/>
    </>
    )
  }
}

export default Logging;