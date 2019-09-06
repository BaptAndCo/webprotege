package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.AddOntologyAnnotationChange;
import edu.stanford.bmir.protege.web.server.index.impl.OntologyAnnotationsIndexImpl;
import edu.stanford.bmir.protege.web.server.index.impl.OntologyIndex;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@RunWith(MockitoJUnitRunner.class)
public class OntologyAnnotationsIndexImpl_TestCase {

    private OntologyAnnotationsIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;


    @Mock
    private OWLAnnotation ontologyAnnotation;

    @Before
    public void setUp() {
        impl = new OntologyAnnotationsIndexImpl();
        impl.handleOntologyChanges(List.of(AddOntologyAnnotationChange.of(ontologyId, ontologyAnnotation)));
    }

    @Test
    public void shouldGetOntologyAnnotations() {
        var ontologyAnnotationsStream = impl.getOntologyAnnotations(ontologyId);
        var ontologyAnnotations = ontologyAnnotationsStream.collect(toSet());
        assertThat(ontologyAnnotations, contains(ontologyAnnotation));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntology() {
        var ontologyAnnotationsStream = impl.getOntologyAnnotations(mock(OWLOntologyID.class));
        assertThat(ontologyAnnotationsStream.count(), is(0L));
    }
}
