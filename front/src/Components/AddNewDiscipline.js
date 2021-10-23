import React, {useState} from 'react'
import {constructObjectDiscipline} from "../Utils/utils";
import {Button, Form, Modal} from "react-bootstrap";
import {Button as FloatButton, Container} from "react-floating-action-button";

function AddNewDiscipline(props) {

    const [show, setShow] = useState(false);

    const handleClose = () => {
        flashInputFields();
        setShow(false);
    };
    const handleShow = () => setShow(true);
    const handleSubmit = () => {
        props.createNewDiscipline(constructObjectDiscipline(inputFields))
        handleClose()
    }

    const [inputFields, setInputFields] = useState({
        name: null
    });

    const setValue = (e) => {
        setInputFields({
            ...inputFields,
            [e.target.name]: e.target.value
        })
    }

    const flashInputFields = (e) => {
        setInputFields({
            name: null
        })
    };

    return (
        <div>
            <>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header>
                        <Modal.Title>Add new Discipline</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <h2>
                            Discipline
                        </h2>
                        <Form>
                            <Form.Group className="mb-3" controlId="form.Name">
                                <Form.Label>Name</Form.Label>
                                <Form.Control
                                    name="name"
                                    type="text"
                                    placeholder="Enter name"
                                    onChange={setValue}
                                />
                            </Form.Group>

                            <Button className="m-1" variant="secondary" onClick={handleClose}>
                                Close
                            </Button>
                            <Button className="m-1" variant="primary" onClick={handleSubmit}>
                                Submit
                            </Button>
                        </Form>
                    </Modal.Body>
                </Modal>
                <Container>
                    <FloatButton
                        rotate={true}
                        onClick={handleShow}
                        tooltip="Add new discipline"
                        styles={{ fontSize: 30 }}
                    >
                        +
                    </FloatButton>
                </Container>
            </>
        </div>
    )
}

AddNewDiscipline.propTypes = {

}

export default AddNewDiscipline

