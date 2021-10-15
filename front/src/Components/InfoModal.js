import React from 'react'
import { Modal, Button } from 'react-bootstrap'

export const InfoModal = (props) => {

    const handleClose = () => {
        props.setShow(false);
    };

    return (
        <Modal show={props.show}>
            <Modal.Header>
                <Modal.Title>Info</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <p>{props.message}</p>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>Close</Button>
            </Modal.Footer>
        </Modal>
    )
}
