package com.bbn.kbp.events2014.bin.QA;

import com.bbn.bue.common.collections.MultimapUtils;
import com.bbn.bue.common.symbols.Symbol;
import com.bbn.kbp.events2014.AnswerKey;
import com.bbn.kbp.events2014.AssessedResponse;
import com.bbn.kbp.events2014.KBPString;
import com.bbn.kbp.events2014.Response;
import com.bbn.kbp.events2014.bin.QA.Warnings.Warning;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Ordering;

/**
 * Created by jdeyoung on 8/24/15.
 */
public final class RenderableCorefEAType {

  private final Symbol type;
  private final Symbol role;
  private final ImmutableSet<RenderableCASGroup> renderableCASGroups;


  private RenderableCorefEAType(final Symbol type, final Symbol role,
      final ImmutableSet<RenderableCASGroup> renderableCASGroups) {
    this.type = type;
    this.role = role;
    this.renderableCASGroups = renderableCASGroups;
  }

  public static RenderableCorefEAType create(final Symbol type, final Symbol role,
      final ImmutableSet<RenderableCASGroup> renderableCASGroups) {
    return new RenderableCorefEAType(type, role, renderableCASGroups);
  }

  public static RenderableCorefEAType createFromAnswerKeyAndWarnings(final Symbol type,
      final Symbol role, final AnswerKey answerKey,
      final ImmutableMultimap<Integer, Warning> warnings) {
    final ImmutableSetMultimap<KBPString, AssessedResponse> kbpStringToRespones =
        ImmutableSetMultimap.copyOf(
            Multimaps.index(answerKey.annotatedResponses(),
                Functions.compose(Response.CASFunction(), AssessedResponse.Response)));
    final ImmutableSet.Builder<RenderableCASGroup> casGroups = ImmutableSet.builder();
    final ImmutableSetMultimap<Integer, AssessedResponse> CASGroupToResponses =
        MultimapUtils.composeToSetMultimap(answerKey.corefAnnotation().clusterIDToMembersMap(),
            kbpStringToRespones);
    for (final Integer CASGroup : warnings.keySet()) {
      casGroups.add(RenderableCASGroup
          .create(CASGroup, CASGroupToResponses.get(CASGroup), ImmutableSet.copyOf(warnings.get(CASGroup))));
    }
    return create(type, role, casGroups.build());
  }

  public String renderToHTML() {
    if(renderableCASGroups.size() == 0) {
      return  "";
    }
    final StringBuilder sb = new StringBuilder();
    sb.append("<div id=\"").append(type).append("/").append(role)
        .append("\" style=display:block >");
    sb.append("<h2>").append(type).append("/").append(role).append("</h2>");
    for (final RenderableCASGroup casGroup : renderableCASGroups) {
      sb.append(casGroup.renderToHTML(String.format("%s/%s", type, role)));
    }
    sb.append("</div>");
    return sb.toString();
  }

  public ImmutableSet<RenderableCASGroup> renderableCASGroups() {
    return renderableCASGroups;
  }

  public String typeAsString() {
    return String.format("%s/%s", type, role);
  }

  public static Ordering<RenderableCorefEAType> byType() {
    return new Ordering<RenderableCorefEAType>() {
      @Override
      public int compare(final RenderableCorefEAType left, final RenderableCorefEAType right) {
        return left.typeAsString().compareTo(right.typeAsString());
      }
    };
  }

  public static Function<RenderableCorefEAType, ImmutableSet<RenderableCASGroup>> renderableCASGroupsAsFunction() {
    return new Function<RenderableCorefEAType, ImmutableSet<RenderableCASGroup>>() {
      @Override
      public ImmutableSet<RenderableCASGroup> apply(final RenderableCorefEAType input) {
        return input.renderableCASGroups();
      }
    };
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final RenderableCorefEAType that = (RenderableCorefEAType) o;

    if (!type.equals(that.type)) {
      return false;
    }
    if (!role.equals(that.role)) {
      return false;
    }
    return renderableCASGroups.equals(that.renderableCASGroups);

  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + role.hashCode();
    result = 31 * result + renderableCASGroups.hashCode();
    return result;
  }
}