import React, {useEffect, useState} from 'react';
import { Button, Modal, Form } from 'react-bootstrap';
import { Button as FloatButton, Container } from 'react-floating-action-button'
import {constructObjectLab, discipline_uri} from '../Utils/utils';
import axios from "axios";
import {parseString} from "xml2js";


function AddNewLab(props) {

    const [show, setShow] = useState(false);
    const [disciplines, setDisciplines] = useState([]);

    const getDisciplineData = async () => {
        axios.get(
            discipline_uri
        ).then (data => {
            parseString(data.data, { explicitArray: false, ignoreAttrs: true }, function (err, result) {
                switch (parseInt(result.disciplines_result.totalDisciplines)) {
                    case 0:
                        setDisciplines([]);
                        break;
                    case 1:
                        setDisciplines([result.disciplines_result.disciplines.discipline]);
                        break;
                    default:
                        setDisciplines(result.disciplines_result.disciplines.discipline)
                        break;
                }
            })
        })
            .catch(function (error) {
                handleClose()
                if (error.response) {
                    // Request made and server responded
                    if (error.response.data)
                        props.catchInfo(error.response.data)
                    else
                        props.catchError()
                } else if (error.request) {
                    // The request was made but no response was received
                    props.catchError()
                } else {
                    // Something happened in setting up the request that triggered an Error
                    props.catchError()
                }
            })
    }

    useEffect(() => {
        if (show)
            getDisciplineData();
    }, [show])

    const handleClose = () => {
        flashInputFields();
        setShow(false);
    };
    const handleShow = () => setShow(true);
    const handleSubmit = () => {
        props.createNewLab(constructObjectLab(inputFields))
        handleClose()
    }

    const [inputFields, setInputFields] = useState({
        name: null,
        minimalPoint: null,
        maximumPoint: null,
        personalQualitiesMaximum: null,
        difficulty: 'VERY_EASY',
        coordinatesX: null,
        coordinatesY: null,
        authorName: null,
        authorWeight: null,
        locationX: null,
        locationY: null,
        locationZ: null,
        locationName: null,
        discipline: -1
    });

    const setValue = (e) => {
        setInputFields({
            ...inputFields,
            [e.target.name]: e.target.value
        })
    }

    const flashInputFields = (e) => {
        setInputFields({
            name: null,
            minimalPoint: null,
            maximumPoint: null,
            personalQualitiesMaximum: null,
            difficulty: 'VERY_EASY',
            coordinatesX: null,
            coordinatesY: null,
            authorName: null,
            authorWeight: null,
            locationX: null,
            locationY: null,
            locationZ: null,
            locationName: null,
            discipline: -1
        })
    };

    return (
        <>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header>
                    <Modal.Title>Add new Lab</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <h2>
                        LabWork
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

                        <Form.Group className="mb-3" controlId="form.MinimalPoint">
                            <Form.Label>Minimal Point</Form.Label>
                            <Form.Control
                                name="minimalPoint"
                                type="number"
                                placeholder="Enter point"
                                onChange={setValue}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="form.MaximumPoint">
                            <Form.Label>Maximal Point</Form.Label>
                            <Form.Control
                                name="maximumPoint"
                                type="number"
                                placeholder="Enter point"
                                onChange={setValue}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="form.personalQualitiesMaximum">
                            <Form.Label>Maximal Point</Form.Label>
                            <Form.Control
                                name="personalQualitiesMaximum"
                                type="number"
                                placeholder="Enter PQs"
                                onChange={setValue}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="form.difficulty">
                            <Form.Label>Difficulty</Form.Label>
                            <Form.Control
                                name="difficulty"
                                as="select"
                                onChange={setValue}
                            >
                                <option value="VERY_EASY">VERY_EASY</option>
                                <option value="EASY">EASY</option>
                                <option value="INSANE">INSANE</option>
                                <option value="HOPELESS">HOPELESS</option>
                                <option value="TERRIBLE">TERRIBLE</option>
                            </Form.Control>
                        </Form.Group>
                        <hr />
                        <h2>
                            Coordinates
                        </h2>
                        <Form.Group className="mb-3" controlId="form.Coordinates.X">
                            <Form.Label>X</Form.Label>
                            <Form.Control
                                name="coordinatesX"
                                type="number"
                                placeholder="Enter point"
                                onChange={setValue}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="form.Coordinates.Y">
                            <Form.Label>Y</Form.Label>
                            <Form.Control
                                name="coordinatesY"
                                type="number"
                                placeholder="Enter point"
                                onChange={setValue}
                            />
                        </Form.Group>

                        <hr />
                        <h2>
                            Author
                        </h2>
                        <Form.Group className="mb-3" controlId="form.Author.Name">
                            <Form.Label>Name</Form.Label>
                            <Form.Control
                                name="authorName"
                                type="text"
                                placeholder="Enter name"
                                onChange={setValue}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="form.Author.Weight">
                            <Form.Label>Weight</Form.Label>
                            <Form.Control
                                name="authorWeight"
                                type="number"
                                placeholder="Enter point"
                                onChange={setValue}
                            />
                        </Form.Group>

                        <hr />
                        <h2>
                            Location
                        </h2>

                        <Form.Group className="mb-3" controlId="form.Location.Name">
                            <Form.Label>Name</Form.Label>
                            <Form.Control
                                name="locationName"
                                type="text"
                                placeholder="Enter name"
                                onChange={setValue}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="form.Location.X">
                            <Form.Label>X</Form.Label>
                            <Form.Control
                                name="locationX"
                                type="number"
                                placeholder="Enter point"
                                onChange={setValue}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="form.Location.Y">
                            <Form.Label>Y</Form.Label>
                            <Form.Control
                                name="locationY"
                                type="number"
                                placeholder="Enter point"
                                onChange={setValue}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="form.Location.Z">
                            <Form.Label>Z</Form.Label>
                            <Form.Control
                                name="locationZ"
                                type="number"
                                placeholder="Enter point"
                                onChange={setValue}
                            />
                        </Form.Group>

                        <hr/>
                        <h2> Discipline </h2>
                        <Form.Group className="mb-3" controlId="form.Discipline.Name">
                            <Form.Control
                                name="discipline"
                                as="select"
                                onChange={setValue}
                            >
                                <option value={-1}>Choose discipline...</option>
                                {disciplines.map(opt => (
                                    <option value={opt.id}>{opt.name}</option>
                                ))}
                            </Form.Control>
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
                    tooltip="Add new lab"
                    styles={{ fontSize: 30 }}
                >
                    +
                </FloatButton>
            </Container>
        </>
    )
}

export default AddNewLab
