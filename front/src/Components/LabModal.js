import React from 'react'
import { Modal, Button } from 'react-bootstrap'

const LabModal = (props) => {
    const handleClose = () => {
        props.setShow(false);
    };

    return (
        <Modal show={props.show}>
            <Modal.Header>
                <Modal.Title>LabWork</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <h2>Labwork</h2>
                Id: {props.labWork.id}<br/>
                Name: {props.labWork.name}<br/>
                CreationDate: {props.labWork.creationDate}<br/>
                MinimalPoint: {props.labWork.minimalPoint}<br/>
                MaximumPoint: {props.labWork.maximumPoint}<br/>
                PersonalQualitiesMaximum: {props.labWork.personalQualitiesMaximum}<br/>
                Difficulty: {props.labWork.difficulty}<br/>
                <hr/>
                <h2>Coordinates</h2>
                X: {props.labWork.coordinates.x}<br/>
                Y: {props.labWork.coordinates.y}<br/>
                <hr/>
                <h2>Author</h2>
                Name: {props.labWork.author.name}<br/>
                Weight: {props.labWork.author.weight}<br/>
                <hr/>
                <h2>Location</h2>
                X: {props.labWork.author.location.x}<br/>
                Y: {props.labWork.author.location.y}<br/>
                Z: {props.labWork.author.location.z}<br/>
                Name: {props.labWork.author.location.name}<br/>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>Close</Button>
            </Modal.Footer>
        </Modal>
    )
}

export default LabModal
