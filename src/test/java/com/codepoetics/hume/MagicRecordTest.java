package com.codepoetics.hume;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import com.codepoetics.hume.api.Property;
import com.codepoetics.hume.api.Record;
import com.codepoetics.spinoza.Compose;
import com.codepoetics.spinoza.api.Lens;
import com.codepoetics.spinoza.matchers.AnInstance;
import com.codepoetics.spinoza.utils.StringLenses;

public class MagicRecordTest {

    public static interface TestRecord extends Record<TestRecord> {
        Property<TestRecord, String> name();
        Property<TestRecord, Integer> age();
    }
    
    private static final TestRecord _ = Magic.lensesOf(TestRecord.class);
    
    @Test public void
    can_build_magic_record_using_magic_builder() {
        TestRecord record = Magic.builderFor(TestRecord.class)
                .with(_.name(), "Dominic")
                .with(_.age(), 38)
                .get();
        
        assertThat(record.name().get(), equalTo("Dominic"));
        assertThat(record.age().get(), equalTo(38));
    }
    
    @Test public void
    magic_record_is_matchable_using_properties_as_lenses() {
        TestRecord record = Magic.builderFor(TestRecord.class)
                .with(_.name(), "Dominic")
                .with(_.age(), 38)
                .get();
        
        assertThat(record, AnInstance.of(TestRecord.class)
                .withProperty(_.name()).equalTo("Dominic")
                .withProperty(_.age()).equalTo(38));
    }
    
    @Test public void
    magic_record_can_be_copied_with_updated_properties() {
        TestRecord record = Magic.builderFor(TestRecord.class)
                .with(_.name(), "Dominic")
                .with(_.age(), 38)
                .get();
        
        TestRecord afterMyBirthday = record.with(_.age(), 39).get();
        
        assertThat(afterMyBirthday, AnInstance.of(TestRecord.class)
                .withProperty(_.name()).equalTo("Dominic")
                .withProperty(_.age()).equalTo(39));
    }
    
    @Test public void
    magic_record_can_be_updated_using_property_as_lens() {
        TestRecord record = Magic.builderFor(TestRecord.class)
                .with(_.name(), "Dominic")
                .with(_.age(), 38)
                .get();
        
        assertThat(_.name().update(record, "Dominique"), AnInstance.of(TestRecord.class)
                .withProperty(_.name()).equalTo("Dominique")
                );
    }
    
    @Test public void
    properties_can_be_composed_with_lenses_for_deep_updates() {
        TestRecord record = Magic.builderFor(TestRecord.class)
                .with(_.name(), "Dominic")
                .with(_.age(), 38)
                .get();
        
        Lens<TestRecord, Character> thirdCharOfName = Compose.theLens(_.name()).with(StringLenses.characterAt(2));
        
        assertThat(thirdCharOfName.update(record, 'g'), AnInstance.of(TestRecord.class)
                .withProperty(_.name()).equalTo("Doginic"));
    }
    
    @Test public void
    magic_records_with_the_same_property_values_are_equal() {
        TestRecord record1 = Magic.builderFor(TestRecord.class)
                .with(_.name(), "Dominic")
                .with(_.age(), 38)
                .get();
        
        TestRecord record2 = Magic.builderFor(TestRecord.class)
                .with(_.name(), "Dominic")
                .with(_.age(), 38)
                .get();
        
        assertThat(record1, equalTo(record2));
    }
}
