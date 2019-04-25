package com.gmail.etauroginskaya.springbootmodule.controller;

import com.gmail.etauroginskaya.service.ItemService;
import com.gmail.etauroginskaya.service.model.ItemDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @Mock
    private BindingResult bindingResult;

    private ItemController itemController;

    private MockMvc mockMvc;

    private List<ItemDTO> items = asList(new ItemDTO(), new ItemDTO());

    @Before
    public void init() {
        itemController = new ItemController(itemService);
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        when(itemService.getItems()).thenReturn(items);
    }

    @Test
    public void allItemsAreAddedToModelForItemsView() {
        Model model = new ExtendedModelMap();
        String items = itemController.showItems(model);
        assertThat(items, equalTo("items"));
        assertThat(model.asMap(), hasEntry("items", this.items));
    }

    @Test
    public void requestForUsersIsSuccessfullyProcessedWithAvailableUsersList() throws Exception {
        this.mockMvc.perform(get("/items.html"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("items", equalTo(items)))
                .andExpect(forwardedUrl("items"));
    }

    @Test
    public void shouldRedirectToResultAfterSuccessfullyAddedItem() {
        String pageAfterAdd = itemController.addItem(new ItemDTO(), bindingResult);
        assertThat(pageAfterAdd, equalTo("redirect:/result-add"));
    }

    @Test
    public void shouldRedirectToFormItemCreateAfterInvalidAddedItem() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String pageAfterAdd = itemController.addItem(new ItemDTO(), bindingResult);
        assertThat(pageAfterAdd, equalTo("item-create"));
    }

    @Test
    public void requestForFormCreateItem() throws Exception {
        this.mockMvc.perform(get("/add.html"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("item-create"));
    }

    @Test
    public void shouldRedirectToFormUpdateFalseAfterInvalidUpdatedItem() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(1L);
        itemDTO.setStatus("test");
        when(itemService.getStatusById(Long.valueOf(itemDTO.getId()))).thenReturn("test");
        String pageAfterAdd = itemController.updateItem(itemDTO);
        assertThat(pageAfterAdd, equalTo("redirect:/update-false"));
    }

    @Test
    public void shouldRedirectToFormUpdateTrueAfterSuccessfullyUpdatedItem() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(1L);
        itemDTO.setStatus("new");
        when(itemService.getStatusById(Long.valueOf(itemDTO.getId()))).thenReturn("old");
        when(itemService.update(Long.valueOf(itemDTO.getId()), itemDTO.getStatus())).thenReturn(1);
        String pageAfterAdd = itemController.updateItem(itemDTO);
        assertThat(pageAfterAdd, equalTo("redirect:/update-true"));
    }
}
