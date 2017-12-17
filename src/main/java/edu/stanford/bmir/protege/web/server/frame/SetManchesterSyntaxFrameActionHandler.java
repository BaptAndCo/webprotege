package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxChangeGenerator;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxChangeGeneratorFactory;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.semanticweb.owlapi.manchestersyntax.renderer.ParserException;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Optional;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class SetManchesterSyntaxFrameActionHandler extends AbstractProjectChangeHandler<Optional<ManchesterSyntaxFrameParseError>, SetManchesterSyntaxFrameAction, SetManchesterSyntaxFrameResult> {

    @Nonnull
    private final GetManchesterSyntaxFrameActionHandler handler;

    @Nonnull
    private final RenderingManager renderer;

    @Nonnull
    private final ManchesterSyntaxChangeGeneratorFactory factory;

    @Inject
    public SetManchesterSyntaxFrameActionHandler(@Nonnull AccessManager accessManager,
                                                 @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                                 @Nonnull HasApplyChanges applyChanges,
                                                 @Nonnull GetManchesterSyntaxFrameActionHandler handler,
                                                 @Nonnull RenderingManager renderer,
                                                 @Nonnull ManchesterSyntaxChangeGeneratorFactory factory) {
        super(accessManager, eventManager, applyChanges);
        this.handler = handler;
        this.renderer = renderer;
        this.factory = factory;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return EDIT_ONTOLOGY;
    }

    @Override
    protected ChangeListGenerator<Optional<ManchesterSyntaxFrameParseError>> getChangeListGenerator(SetManchesterSyntaxFrameAction action,
                                                               ExecutionContext executionContext) {
        return factory.create(action.getFromRendering(),
                       action.getToRendering(),
                       action);
    }

    @Override
    protected ChangeDescriptionGenerator<Optional<ManchesterSyntaxFrameParseError>> getChangeDescription(SetManchesterSyntaxFrameAction action,
                                                                    ExecutionContext executionContext) {
        String changeDescription = "Edited description of " + renderer.getShortForm(action.getSubject()) + ".";
        Optional<String> commitMessage = action.getCommitMessage();
        if(commitMessage.isPresent()) {
            changeDescription += "\n" + commitMessage.get();
        }
        return new FixedMessageChangeDescriptionGenerator<>(changeDescription);
    }

    @Override
    protected SetManchesterSyntaxFrameResult createActionResult(ChangeApplicationResult<Optional<ManchesterSyntaxFrameParseError>> result,
                                                                SetManchesterSyntaxFrameAction action,
                                                                ExecutionContext executionContext,
                                                                EventList<ProjectEvent<?>> eventList) {

        if(result.getSubject().isPresent()) {
            throw new SetManchesterSyntaxFrameException(result.getSubject().get());
        }
        else {
            GetManchesterSyntaxFrameAction ac = new GetManchesterSyntaxFrameAction(action.getProjectId(),
                                                                                   action.getSubject());
            GetManchesterSyntaxFrameResult frame = handler.execute(ac, executionContext);
            String reformattedFrame = frame.getManchesterSyntax();
            return new SetManchesterSyntaxFrameResult(eventList, reformattedFrame);
        }
    }

    @Override
    public Class<SetManchesterSyntaxFrameAction> getActionClass() {
        return SetManchesterSyntaxFrameAction.class;
    }


}
