package edu.stanford.bmir.protege.web.shared.hierarchy;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge Stanford Center for Biomedical Informatics Research 28 Nov 2017
 */
public class GetHierarchyPathsToRootAction extends AbstractHasProjectAction<GetHierarchyPathsToRootResult> {

    private OWLEntity entity;

    private HierarchyId hierarchyId;

    public GetHierarchyPathsToRootAction(@Nonnull ProjectId projectId,
                                         @Nonnull OWLEntity entity,
                                         @Nonnull HierarchyId hierarchyId) {
        super(projectId);
        this.entity = checkNotNull(entity);
        this.hierarchyId = checkNotNull(hierarchyId);
    }

    @GwtSerializationConstructor
    private GetHierarchyPathsToRootAction() {
    }

    @Nonnull
    public OWLEntity getEntity() {
        return entity;
    }

    @Nonnull
    public HierarchyId getHierarchyId() {
        return hierarchyId;
    }
}