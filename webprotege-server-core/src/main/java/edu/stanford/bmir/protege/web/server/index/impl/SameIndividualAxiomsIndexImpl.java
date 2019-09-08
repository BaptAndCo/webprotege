package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.MultimapBuilder;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.index.SameIndividualAxiomsIndex;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-10
 */
public class SameIndividualAxiomsIndexImpl implements SameIndividualAxiomsIndex, RequiresOntologyChangeNotification {

    @Nonnull
    private final AxiomMultimapIndex<OWLIndividual, OWLSameIndividualAxiom> index;

    @Inject
    public SameIndividualAxiomsIndexImpl() {
        index = AxiomMultimapIndex.createWithNaryKeyValueExtractor(OWLSameIndividualAxiom.class,
                                                                   OWLSameIndividualAxiom::getIndividuals,
                                                                   MultimapBuilder.hashKeys()
                                                                                  .arrayListValues()
                                                                                  .build());
    }

    @Nonnull
    @Override
    public Stream<OWLSameIndividualAxiom> getSameIndividualAxioms(@Nonnull OWLIndividual individual,
                                                                  @Nonnull OWLOntologyID ontologyId) {
        checkNotNull(individual);
        checkNotNull(ontologyId);
        return index.getAxioms(individual, ontologyId);
    }

    @Override
    public void handleOntologyChanges(@Nonnull List<OntologyChange> changes) {
        index.handleOntologyChanges(changes);
    }
}
