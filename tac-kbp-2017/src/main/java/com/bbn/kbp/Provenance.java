package com.bbn.kbp;

import com.bbn.bue.common.TextGroupImmutable;
import com.bbn.bue.common.strings.offsets.CharOffset;
import com.bbn.bue.common.strings.offsets.OffsetRange;
import com.bbn.bue.common.symbols.Symbol;

import com.google.common.collect.ImmutableSet;

import org.immutables.func.Functional;
import org.immutables.value.Value;

import java.util.Set;

/**
 * A possible fourth field of an assertion that refers back to the part of a document where the
 * justification for the assertion is found. The format is "DocumentID:Span" where a span is a
 * dash-separated span of offsets (E.G. "1-10"). An assertion with multiple provenances must
 * separate them with a comma (E.G. "doc1:1-10,doc4:23:35,doc3:12:42"). Groups of spans for a given
 * provenance should be separated by a semi-colon (E.G. "doc2:12-14;18-31"). Each provenance span
 * must come from a single document; no provenance should span multiple documents. Each span must
 * be at most 200 UTF-8 characters.
 * <p>
 * For a list of the types of allowable provenances, see section 2.4 Provenance in the 2017
 * revisions to the KBP TAC 2016 ColdStart task description. For an explanation of which provenance
 * type can be used with which assertion, see subsections A-G of section 2.4 Provenance in the 2017
 * revisions to the KBP TAC 2016 ColdStart task description.
 */
@TextGroupImmutable
@Value.Immutable
@Functional
public abstract class Provenance {

  public abstract Symbol documentId();

  public abstract ImmutableSet<OffsetRange<CharOffset>> offsets();

  public static Provenance of(final Symbol documentId, final Set<OffsetRange<CharOffset>> offsets) {
    return ImmutableProvenance.builder().documentId(documentId).offsets(offsets).build();
  }

}
