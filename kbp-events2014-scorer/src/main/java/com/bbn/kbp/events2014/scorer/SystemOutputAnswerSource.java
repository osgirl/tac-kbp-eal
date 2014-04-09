package com.bbn.kbp.events2014.scorer;

import java.util.Set;

import com.bbn.bue.common.diff.AnswerSource;
import com.bbn.kbp.events2014.Response;
import com.bbn.kbp.events2014.SystemOutput;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import static com.google.common.base.Preconditions.checkNotNull;

public class SystemOutputAnswerSource<Answerable> implements AnswerSource<Answerable, Response> {
	private final SystemOutput systemOutput;
	private final Multimap<Answerable, Response> equivalenceClasses;

	private SystemOutputAnswerSource(final SystemOutput systemOutput,
		final Multimap<Answerable, Response> equivalenceClasses)
	{
		this.systemOutput = checkNotNull(systemOutput);
		this.equivalenceClasses = ImmutableMultimap.copyOf(equivalenceClasses);
	}

	public static <Answerable> SystemOutputAnswerSource<Answerable> forAnswerable(
		final SystemOutput systemOutput, final Function<Response, Answerable> answerableExtractor)
	{
		return new SystemOutputAnswerSource<Answerable>(systemOutput,
			Multimaps.index(systemOutput.responses(), answerableExtractor));
	}

	@Override
	public Set<Answerable> answerables() {
		return equivalenceClasses.keySet();
	}

	@Override
	public Set<Response> answers(final Answerable answerable) {
		return ImmutableSet.copyOf(equivalenceClasses.get(answerable));
	}

	public SystemOutput systemOutput() {
		return systemOutput;
	}
}